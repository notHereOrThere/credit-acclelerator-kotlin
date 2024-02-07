package com.example.deal.service

import com.example.credit.application.model.*
import com.example.deal.dto.EmailDto
import com.example.deal.entity.Application
import com.example.deal.entity.Client
import com.example.deal.entity.enums.ApplicationStatus
import com.example.deal.entity.enums.ChangeType
import com.example.deal.entity.enums.Status
import com.example.deal.entity.inner.StatusHistory
import com.example.deal.exception.UserException
import com.example.deal.feign.ConveyerFeignClient
import com.example.deal.kafka.KafkaProducerService
import com.example.deal.mapper.DealMapper
import com.example.deal.repository.ApplicationRepository
import com.example.deal.repository.ClientRepository
import com.example.deal.repository.CreditRepository
import feign.FeignException
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Consumer
import javax.persistence.EntityNotFoundException

@Service
class DealServiceImpl @Autowired constructor
    (
        private val feignClient: ConveyerFeignClient,
        private val dealMapper: DealMapper,
        private val clientRepository: ClientRepository,
        private val creditRepository: CreditRepository,
        private val applicationRepository: ApplicationRepository,
        private val kafkaProducerService: KafkaProducerService
) : DealService {
    @Value("\${topic.finish-registration}")
    private val finishRegistration: String? = null

    @Value("\${topic.create-documents}")
    private val createDocument: String? = null

    @Value("\${topic.send-documents}")
    private val sendDocuments: String? = null

    @Value("\${topic.send-ses}")
    private val sendSes: String? = null

    @Value("\${topic.credit-issued}")
    private val creditIssued: String? = null

    @Value("\${topic.application-denied}")
    private val applicationDenied: String? = null


    override fun calculateLoanConditions(loanApplicationRequestDTO: LoanApplicationRequestDTO?): List<LoanOfferDTO?> {
        val application = Application()
        application.creationDate = Date()
        var loanOfferDTOs: List<LoanOfferDTO?>? = null
        loanOfferDTOs = try {
            feignClient!!.calculateLoanOffers(loanApplicationRequestDTO)
        } catch (e: FeignException.BadRequest) {
            val emailDto = EmailDto()
            emailDto.fio = loanApplicationRequestDTO!!.lastName + " " + loanApplicationRequestDTO.firstName
            if (!StringUtils.isBlank(loanApplicationRequestDTO.middleName)) {
                emailDto.fio = emailDto.fio + " " + loanApplicationRequestDTO.middleName
            }
            emailDto.email = loanApplicationRequestDTO.email
            emailDto.emailText = "В ходе выполнения перскоринга выявлена ошибка: " + e.message
            kafkaProducerService!!.send(applicationDenied, emailDto)
            buildApplicationHistory(application, "Отказ")
            application.status = (ApplicationStatus.CC_DENIED)
            applicationRepository!!.save(application)
            throw UserException(e.message)
        } catch (e: FeignException) {
            throw RuntimeException(e.message, e)
        }
        val client = dealMapper!!.loanApplicationRequestDtoToClientEntity(loanApplicationRequestDTO)
        client?.let {
            clientRepository?.save(it)
        }

        with(application) {
            status = ApplicationStatus.PREAPPROVAL
            this.client = client
        }
        val applicationId: Long? = applicationRepository.save(application).applicationId
        loanOfferDTOs!!.forEach(Consumer { e: LoanOfferDTO? ->
            e!!.applicationId = applicationId
        })
        return loanOfferDTOs
    }

    override fun chooseLoanOffer(loanOfferDTO: LoanOfferDTO?) {
        val application =
            applicationRepository!!.findById(loanOfferDTO!!.applicationId).orElseThrow { EntityNotFoundException() }!!
        buildApplicationHistory(application, "Предварительное подтверждение")
        val loanOffer = dealMapper!!.loanOfferDtoToLoanOfferEntity(loanOfferDTO)
        application.appliedOffer = loanOffer
        application.status = ApplicationStatus.APPROVED
        applicationRepository.save(application)
        val emailDto = EmailDto()

        with(emailDto) {
            applicationId = application.applicationId
            fio =
                application.client?.lastName + " " + application.client?.firstName + " " + application.client?.middleName
            email = application.client?.email
            emailText = "После завершения регистрации, мы сможем завершить подсчет условий кредита."
        }
        kafkaProducerService!!.send(finishRegistration, emailDto)
    }

    override fun finishRegistrationAndCalculateLoan(
        applicationId: Long?,
        finishRegistrationRequestDTO: FinishRegistrationRequestDTO?
    ) {
        val application = applicationRepository!!.findById(applicationId!!).orElseThrow { EntityNotFoundException() }!!
        val employment = dealMapper!!.employmentDtoToEmploymentEntity(finishRegistrationRequestDTO!!.employment)
        val client: Client? = application.client
        with(client) {
            client?.employment = employment
            this?.passport?.passportIssueDate = finishRegistrationRequestDTO.passportIssueDate
            this?.passport?.passportIssueBranch = finishRegistrationRequestDTO.passportIssueBranch
            this?.dependentAmount = finishRegistrationRequestDTO.dependentAmount
        }
        client?.let {
            clientRepository?.save(client)
        }

        val scoringDataDTO = prepareScoringDataDto(finishRegistrationRequestDTO, application)
        var creditDTO: CreditDTO? = null
        creditDTO = try {
            feignClient!!.performLoanCalculation(scoringDataDTO)
        } catch (e: FeignException.BadRequest) {
            application.status = ApplicationStatus.CC_DENIED
            buildApplicationHistory(application, "Скоринг не пройден")
            applicationRepository.save(application)
            val emailDto = EmailDto()

            with(emailDto) {
                this.applicationId = application.applicationId
                fio =
                    application.client?.lastName + " " + application.client?.firstName + " " + application.client?.middleName
                email = application.client?.email
                emailText = "Произошла ошибка в ходе выполнения скоринга, данные некорректны."
            }
            kafkaProducerService!!.send(applicationDenied, emailDto)
            throw UserException(e.message)
        } catch (e: FeignException) {
            throw RuntimeException(e.message, e)
        }
        val credit = dealMapper.creditDtoToCreditEntity(creditDTO)
        credit?.paymentSchedule?.forEach { e -> e.credit = credit }
        credit?.creditStatus = Status.CALCULATED

        credit?.let {
            creditRepository!!.save(credit)
        }
        application.status = ApplicationStatus.CC_APPROVED
        application.credit = credit
        buildApplicationHistory(application, "Подтверждение")
        applicationRepository.save(application)
        val emailDto = EmailDto()
        with(emailDto) {
            this.applicationId = application.applicationId
            fio =
                application.client?.lastName + " " + application.client?.firstName + " " + application.client?.middleName
            email = application.client?.email
            emailText = "Кредит расчитан, вы можете получить выгрузку списка документов на свою почту."
        }
        kafkaProducerService!!.send(createDocument, emailDto)
    }

    override fun sendDocuments(applicationId: Long?) {
        val application = applicationRepository!!.findById(applicationId!!).orElseThrow { EntityNotFoundException() }!!
        buildApplicationHistory(application, "Подготовка документов.")
        application.status = ApplicationStatus.PREPARE_DOCUMENT
        applicationRepository.save(application)
        kafkaProducerService!!.send(sendDocuments, application)
    }

    override fun signDocuments(applicationId: Long?) {
        val sesDto = SesDto()
        val uuid = UUID.randomUUID()
        val application = applicationRepository!!.findById(applicationId!!).orElseThrow { EntityNotFoundException() }!!
        application.sesCode = uuid.toString()
        buildApplicationHistory(application, "Подготовка документов.")
        applicationRepository.save(application)
        sesDto.sesCode = uuid.toString()

        val emailDto = EmailDto()
        with(emailDto) {
            this.applicationId = application.applicationId
            fio =
                application.client?.lastName + " " + application.client?.firstName + " " + application.client?.middleName
            email = application.client?.email
            emailText = "Никому не показывайте, ваш СЭС код - $uuid"
        }
        kafkaProducerService!!.send(sendSes, emailDto)
    }

    override fun codeDocuments(applicationId: Long?, sesDto: SesDto?) {
        val application = applicationRepository!!.findById(applicationId!!).orElseThrow { EntityNotFoundException() }!!
        if (!application.sesCode.equals(sesDto!!.sesCode)) {
            val emailDto = EmailDto()
            with(emailDto) {
                this.applicationId = application.applicationId
                fio =
                    application.client?.lastName + " " + application.client?.firstName + " " + application.client?.middleName
                email = application.client?.email
                emailText = "СЭС коды не совпадают."
            }
            kafkaProducerService!!.send(applicationDenied, emailDto)
            return
        }
        application.status = ApplicationStatus.CREDIT_ISSUED
        buildApplicationHistory(application, "Кредит выдан")
        applicationRepository.save(application)
        val emailDto = EmailDto()
        with(emailDto) {
            this.applicationId = application.applicationId
            fio =
                application.client?.lastName + " " + application.client?.firstName + " " + application.client?.middleName
            email = application.client?.email
            emailText = "Кредит успешно выдан."
        }
        kafkaProducerService!!.send(creditIssued, emailDto)
    }

    override val allApplications: List<Application?>
        get() = applicationRepository.findAll()

    override fun getApplicationById(applicationId: Long?): Application? {
        return applicationRepository.findById(applicationId!!).orElseThrow { EntityNotFoundException() }
    }

    private fun buildApplicationHistory(application: Application, text: String) {
        val statusHistory = StatusHistory()
        with(statusHistory) {
            changeType = ChangeType.AUTOMATIC
            time = Date()
            status = text
            this.application = application
        }
        application.statusHistory.plus(statusHistory)
    }

    private fun prepareScoringDataDto(
        finishRegistrationRequestDTO: FinishRegistrationRequestDTO?,
        application: Application
    ): ScoringDataDTO {
        val scoringDataDTO = ScoringDataDTO()
        with(scoringDataDTO) {
            gender = mapGenderFRRDtoSDD(finishRegistrationRequestDTO!!.gender)
            maritalStatus = mapMaritalStatusEnumFRRDtoSDD(finishRegistrationRequestDTO.maritalStatus)
            dependentAmount = finishRegistrationRequestDTO.dependentAmount
            passportIssueDate = finishRegistrationRequestDTO.passportIssueDate
            passportIssueBranch = finishRegistrationRequestDTO.passportIssueBranch
            account = finishRegistrationRequestDTO.account
            employment = finishRegistrationRequestDTO.employment
            amount = application.appliedOffer?.totalAmount
            term = application.appliedOffer?.term
            firstName = application.client?.firstName
            lastName = application.client?.lastName
            middleName = application.client?.middleName
            birthdate = application.client?.birthdate
            passportNumber = application.client?.passport?.passportNum
            passportSeries = application.client?.passport?.passportSer
            isInsuranceEnabled = application.appliedOffer?.isInsuranceEnabled
            isSalaryClient = application.appliedOffer?.isSalaryClient
        }
        return scoringDataDTO
    }

    private fun mapGenderFRRDtoSDD(genderEnum: FinishRegistrationRequestDTO.GenderEnum): ScoringDataDTO.GenderEnum {
        return when (genderEnum) {
            FinishRegistrationRequestDTO.GenderEnum.NON_BINARY -> ScoringDataDTO.GenderEnum.NON_BINARY
            FinishRegistrationRequestDTO.GenderEnum.FEMALE -> ScoringDataDTO.GenderEnum.FEMALE
            else -> ScoringDataDTO.GenderEnum.MALE
        }
    }

    private fun mapMaritalStatusEnumFRRDtoSDD(maritalStatusEnum: FinishRegistrationRequestDTO.MaritalStatusEnum): ScoringDataDTO.MaritalStatusEnum {
        return when (maritalStatusEnum) {
            FinishRegistrationRequestDTO.MaritalStatusEnum.WIDOW_WIDOWER -> ScoringDataDTO.MaritalStatusEnum.WIDOW_WIDOWER
            FinishRegistrationRequestDTO.MaritalStatusEnum.SINGLE -> ScoringDataDTO.MaritalStatusEnum.SINGLE
            FinishRegistrationRequestDTO.MaritalStatusEnum.DIVORCE -> ScoringDataDTO.MaritalStatusEnum.DIVORCE
            else -> ScoringDataDTO.MaritalStatusEnum.MARRIED
        }
    }
}
