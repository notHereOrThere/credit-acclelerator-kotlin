package com.example.deal.service

import com.example.credit.application.model.*
import com.example.deal.entity.Application
import com.example.deal.entity.Client
import com.example.deal.entity.Credit
import com.example.deal.entity.inner.LoanOffer
import com.example.deal.entity.inner.Passport
import com.example.deal.exception.UserException
import com.example.deal.feign.ConveyerFeignClient
import com.example.deal.kafka.KafkaProducerService
import com.example.deal.mapper.DealMapper
import com.example.deal.repository.ApplicationRepository
import com.example.deal.repository.ClientRepository
import com.example.deal.repository.CreditRepository
import feign.FeignException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.Optional
import kotlin.test.assertNotNull
import org.mockito.kotlin.anyOrNull
import scala.App
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class DealServiceImplTest {

    @Mock
    private var feignClient: ConveyerFeignClient? = mock()

    @Mock
    private lateinit var dealMapper: DealMapper

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var creditRepository: CreditRepository

    @Mock
    private lateinit var applicationRepository: ApplicationRepository

    @Mock
    private lateinit var kafkaProducerService: KafkaProducerService

    @InjectMocks
    private lateinit var dealService: DealServiceImpl

    private lateinit var loanApplicationRequestDTO: LoanApplicationRequestDTO

    @BeforeEach
    fun setup() {
        loanApplicationRequestDTO = LoanApplicationRequestDTO().apply {
            firstName = "Test"
            lastName = "User"
            email = "test@example.com"
            // Дополните поля DTO в соответствии с вашей моделью
        }
    }

    @Test
    fun `calculateLoanConditions should save application and return loan offers`() {
        val expectedLoanOffers = listOf(LoanOfferDTO())
        whenever(feignClient!!.calculateLoanOffers(any())).thenReturn(expectedLoanOffers)
        whenever(dealMapper.loanApplicationRequestDtoToClientEntity(any())).thenReturn(Client())

        var applicationMock: Application = Application()
        applicationMock.applicationId = 1
//        doReturn(applicationMock).whenever(applicationRepository.save(anyOrNull()))
        doReturn(applicationMock).whenever(applicationRepository).save(anyOrNull())

        val result = dealService.calculateLoanConditions(loanApplicationRequestDTO)

        // Проверка
        verify(clientRepository).save(any())
        verify(applicationRepository).save(any())
        assert(result == expectedLoanOffers)
    }

    @Test
    fun `calculateLoanConditions should send email and throw UserException on FeignException BadRequest`() {
        // Подготовка
        doThrow(FeignException.BadRequest::class).whenever(feignClient)!!.calculateLoanOffers(any())

        // Выполнение и проверка
        assertFailsWith<UserException> {
            dealService.calculateLoanConditions(loanApplicationRequestDTO)
        }

        verify(kafkaProducerService).send(anyOrNull(), any())
        verify(applicationRepository).save(any())
    }

    @Test
    fun `test calculate loan conditions`() {
        val requestDTO: LoanApplicationRequestDTO? = null
        val mockLoanOffers = mutableListOf<LoanOfferDTO>()
        val mockClient = Client()
        val mockApplication = Application()
        mockApplication.applicationId = 1L


        whenever(feignClient!!.calculateLoanOffers(requestDTO)).thenReturn(mockLoanOffers)
        whenever(dealMapper.loanApplicationRequestDtoToClientEntity(requestDTO)).thenReturn(mockClient)
//        doReturn(mockApplication).
        `when`(applicationRepository.save(anyOrNull())).thenReturn(mockApplication)


        val result = dealService.calculateLoanConditions(requestDTO)

        assertNotNull(result)
        verify(feignClient)!!.calculateLoanOffers(requestDTO)
        verify(dealMapper).loanApplicationRequestDtoToClientEntity(requestDTO)
        verify(clientRepository).save(mockClient)
        verify(applicationRepository).save(any())
    }

    @Test
    fun `test choose loan offer`() {
        val loanOfferDTO = LoanOfferDTO().apply { applicationId = 1L }
        val mockApplication = Application()
        val mockLoanOffer = LoanOffer()
        var mockClient = Client()
        mockApplication.client = mockClient

        whenever(applicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication) as Optional<Application?>)

        whenever(dealMapper.loanOfferDtoToLoanOfferEntity(loanOfferDTO)).thenReturn(mockLoanOffer)

        dealService.chooseLoanOffer(loanOfferDTO)

        verify(applicationRepository).findById(1L)
        verify(dealMapper).loanOfferDtoToLoanOfferEntity(loanOfferDTO)
        verify(applicationRepository).save(mockApplication)
    }

    @Test
    fun `test finish registration and calculate loan with spy`() {
        val applicationId = 1L
        val requestDTO = FinishRegistrationRequestDTO()
        val mockApplication = spy(Application())
        val mockClient = spy(Client().apply { passport = Passport() })
        mockApplication.client = mockClient
        mockApplication.appliedOffer = LoanOffer()
        val mockCreditDTO = CreditDTO()
        val mockCredit = spy(Credit())

        val employmentDTO = EmploymentDTO().apply {
            employmentStatus = EmploymentDTO.EmploymentStatusEnum.EMPLOYED
            position = EmploymentDTO.PositionEnum.MIDDLE_MANAGER
        }
        requestDTO.employment = employmentDTO
        requestDTO.gender = FinishRegistrationRequestDTO.GenderEnum.MALE
        requestDTO.maritalStatus = FinishRegistrationRequestDTO.MaritalStatusEnum.MARRIED

        whenever(applicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication) as Optional<Application?>)
        whenever(dealMapper.creditDtoToCreditEntity(anyOrNull())).thenReturn(mockCredit)

//        whenever(feignClient.performLoanCalculation(any())).thenReturn(mockCreditDTO)

        dealService.finishRegistrationAndCalculateLoan(applicationId, requestDTO)

        verify(applicationRepository).findById(applicationId)
        verify(clientRepository).save(any())
        verify(creditRepository).save(mockCredit)
        verify(applicationRepository).save(mockApplication)
    }
}