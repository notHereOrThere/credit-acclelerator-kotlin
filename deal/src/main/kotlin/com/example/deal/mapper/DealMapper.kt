package com.example.deal.mapper

import com.example.credit.application.model.*
import com.example.deal.entity.Client
import com.example.deal.entity.Credit
import com.example.deal.entity.enums.EmploymentStatus
import com.example.deal.entity.enums.Position
import com.example.deal.entity.inner.Employment
import com.example.deal.entity.inner.LoanOffer
import com.example.deal.entity.inner.Passport
import com.example.deal.entity.inner.PaymentSchedule
import org.springframework.stereotype.Component

@Component
class DealMapper {
    fun loanApplicationRequestDtoToClientEntity(loanApplicationRequestDTO: LoanApplicationRequestDTO?): Client? {
        if (loanApplicationRequestDTO == null) {
            return null
        }
        val client = Client()
        client.passport = loanApplicationRequestDTOToPassport(loanApplicationRequestDTO)
        client.lastName = loanApplicationRequestDTO.lastName
        client.firstName = loanApplicationRequestDTO.firstName
        client.middleName = loanApplicationRequestDTO.middleName
        client.birthdate = loanApplicationRequestDTO.birthdate
        client.email = loanApplicationRequestDTO.email
        return client
    }

    fun loanOfferDtoToLoanOfferEntity(loanOfferDTO: LoanOfferDTO?): LoanOffer? {
        if (loanOfferDTO == null) {
            return null
        }
        val loanOffer = LoanOffer()
        loanOffer.requestedAmount = loanOfferDTO.requestedAmount
        loanOffer.totalAmount = loanOfferDTO.totalAmount
        loanOffer.term = loanOfferDTO.term
        loanOffer.monthlyPayment = loanOfferDTO.monthlyPayment
        loanOffer.rate = loanOfferDTO.rate
        return loanOffer
    }

    fun creditDtoToCreditEntity(creditDTO: CreditDTO?): Credit? {
        if (creditDTO == null) {
            return null
        }
        val credit = Credit()
        credit.amount = creditDTO.amount
        credit.term = creditDTO.term
        credit.monthlyPayment = creditDTO.monthlyPayment
        credit.rate = creditDTO.rate
        credit.psk =creditDTO.psk
        credit.paymentSchedule = paymentScheduleElementListToPaymentScheduleList(creditDTO.paymentSchedule)
        return credit
    }

    fun employmentDtoToEmploymentEntity(employmentDTO: EmploymentDTO?): Employment? {
        if (employmentDTO == null) {
            return null
        }
        val employment = Employment()
        employment.position = mapPosition(employmentDTO.position)
        employment.employmentStatus = mapEmploymentStatus(employmentDTO.employmentStatus)
        employment.employerINN = employmentDTO.employerINN
        employment.salary = employmentDTO.salary
        employment.workExperienceTotal = employmentDTO.workExperienceTotal
        employment.workExperienceCurrent = employmentDTO.workExperienceCurrent
        return employment
    }

    fun mapPosition(positionEnum: EmploymentDTO.PositionEnum?): Position? {
        if (positionEnum == null) {
            return null
        }
        val position: Position
        position = when (positionEnum) {
            EmploymentDTO.PositionEnum.WORKER -> Position.WORKER
            EmploymentDTO.PositionEnum.MIDDLE_MANAGER -> Position.MIDDLE_MANAGER
            EmploymentDTO.PositionEnum.TOP_MANAGER -> Position.TOP_MANAGER
            EmploymentDTO.PositionEnum.OWNER -> Position.OWNER
            else -> throw IllegalArgumentException("Unexpected enum constant: $positionEnum")
        }
        return position
    }

    fun mapEmploymentStatus(employmentStatusEnum: EmploymentDTO.EmploymentStatusEnum?): EmploymentStatus? {
        if (employmentStatusEnum == null) {
            return null
        }
        val employmentStatus: EmploymentStatus
        employmentStatus = when (employmentStatusEnum) {
            EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED -> EmploymentStatus.UNEMPLOYED
            EmploymentDTO.EmploymentStatusEnum.SELF_EMPLOYED -> EmploymentStatus.SELF_EMPLOYED
            EmploymentDTO.EmploymentStatusEnum.EMPLOYED -> EmploymentStatus.EMPLOYED
            EmploymentDTO.EmploymentStatusEnum.BUSINESS_OWNER -> EmploymentStatus.BUSINESS_OWNER
            else -> throw IllegalArgumentException("Unexpected enum constant: $employmentStatusEnum")
        }
        return employmentStatus
    }

    protected fun loanApplicationRequestDTOToPassport(loanApplicationRequestDTO: LoanApplicationRequestDTO?): Passport? {
        if (loanApplicationRequestDTO == null) {
            return null
        }
        val passport = Passport()
        passport.passportSer = loanApplicationRequestDTO.passportSeries
        passport.passportNum = loanApplicationRequestDTO.passportNumber
        return passport
    }

    protected fun paymentScheduleElementToPaymentSchedule(paymentScheduleElement: PaymentScheduleElement): PaymentSchedule {
        val paymentSchedule = PaymentSchedule()
        paymentSchedule.number = paymentScheduleElement.number
        paymentSchedule.date = paymentScheduleElement.date
        paymentSchedule.totalPayment = paymentScheduleElement.totalPayment
        paymentSchedule.interestPayment = paymentScheduleElement.interestPayment
        paymentSchedule.debtPayment = paymentScheduleElement.debtPayment
        paymentSchedule.remainingDebt = paymentScheduleElement.remainingDebt
        return paymentSchedule
    }

    protected fun paymentScheduleElementListToPaymentScheduleList(list: List<PaymentScheduleElement>): List<PaymentSchedule> {
        val list1: MutableList<PaymentSchedule> = ArrayList<PaymentSchedule>(list.size)
        for (paymentScheduleElement in list) {
            list1.add(paymentScheduleElementToPaymentSchedule(paymentScheduleElement))
        }
        return list1
    }
}
