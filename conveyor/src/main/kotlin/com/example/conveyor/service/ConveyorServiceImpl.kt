package com.example.conveyor.service

import com.example.conveyor.model.enums.EmploymentStatus
import com.example.conveyor.model.exception.UserException
import com.example.conveyor.service.prescoring.*
import com.example.credit.application.model.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class ConveyorServiceImpl : ConveyorService{

    private val preScoring: MutableList<PreScoring> = mutableListOf()

    @Value("\${base.rate}")
    private lateinit var baseRate: String

    init {
        preScoring.add(PreScoringValidator1())
        preScoring.add(PreScoringValidator2())
        preScoring.add(PreScoringValidator3())
        preScoring.add(PreScoringValidator4())
        preScoring.add(PreScoringValidator5())
        preScoring.add(PreScoringValidator6())
    }

    override fun offers(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        val results = preScoring.map { it.preScore(loanApplicationRequestDTO) }
        if (results.any { !it.getIsValid() }) {
            throw UserException("Прескоринг показал следующие ошибки: ${results.filterNot { it.getIsValid() }.joinToString { it.toString() }}")
        }

        return generateOffers(loanApplicationRequestDTO).sortedByDescending { it.rate }
    }

    override fun calculation(scoringDataDTO: ScoringDataDTO): CreditDTO {
        if (validateRefuse(scoringDataDTO)) {
            throw UserException("Отказ в предоставлении кредита")
        }

        val rate = calculateRate(scoringDataDTO)
        val monthlyPay = calculateMonthlyPayment(scoringDataDTO.amount, rate, scoringDataDTO.term)

        var creditDTO = CreditDTO()
        with(creditDTO) {
            amount=scoringDataDTO.amount
            amount = scoringDataDTO.amount
            term = scoringDataDTO.term
            monthlyPayment = monthlyPay
            creditDTO.rate = rate
            psk = calculatePSK(scoringDataDTO.amount, scoringDataDTO.term, rate)
            isInsuranceEnabled = scoringDataDTO.isInsuranceEnabled
            isSalaryClient = scoringDataDTO.isSalaryClient
            paymentSchedule = generatePaymentSchedule(scoringDataDTO.amount, rate, scoringDataDTO.term, monthlyPay)
        }
        return creditDTO
    }
    private fun calculatePSK(amount: BigDecimal, term: Int, rate: BigDecimal): BigDecimal {
        val monthlyPayment = calculateMonthlyPayment(amount, rate, term)
        return monthlyPayment * BigDecimal(term)
    }

    private fun generateOffers(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        val result: MutableList<LoanOfferDTO> = java.util.ArrayList()
        run {
            val loanOfferOne = LoanOfferDTO()
            result.add(loanOfferOne)
            loanOfferOne.requestedAmount = loanApplicationRequestDTO.amount
            loanOfferOne.term = loanApplicationRequestDTO.term
            loanOfferOne.isInsuranceEnabled = false
            loanOfferOne.isSalaryClient = false
            loanOfferOne.totalAmount = calculateTotalAmount(
                loanOfferOne.requestedAmount,
                loanOfferOne.isInsuranceEnabled,
                loanOfferOne.term
            )
            loanOfferOne.rate = calculateRate(loanOfferOne.isSalaryClient, loanOfferOne.isInsuranceEnabled)
            loanOfferOne.monthlyPayment = calculateMonthlyPayment(
                loanOfferOne.totalAmount,
                loanOfferOne.rate,
                loanOfferOne.term
            )
        }
        run {
            val loanOfferTwo = LoanOfferDTO()
            result.add(loanOfferTwo)
            loanOfferTwo.requestedAmount = loanApplicationRequestDTO.amount
            loanOfferTwo.term = loanApplicationRequestDTO.term
            loanOfferTwo.isInsuranceEnabled = false
            loanOfferTwo.isSalaryClient = true
            loanOfferTwo.totalAmount = calculateTotalAmount(
                loanOfferTwo.requestedAmount,
                loanOfferTwo.isInsuranceEnabled,
                loanOfferTwo.term
            )
            loanOfferTwo.rate = calculateRate(loanOfferTwo.isSalaryClient, loanOfferTwo.isInsuranceEnabled)
            loanOfferTwo.monthlyPayment = calculateMonthlyPayment(
                loanOfferTwo.totalAmount,
                loanOfferTwo.rate,
                loanOfferTwo.term
            )
        }
        run {
            val loanOfferThree = LoanOfferDTO()
            result.add(loanOfferThree)
            loanOfferThree.requestedAmount = loanApplicationRequestDTO.amount
            loanOfferThree.term = loanApplicationRequestDTO.term
            loanOfferThree.isInsuranceEnabled = true
            loanOfferThree.isSalaryClient = false
            loanOfferThree.totalAmount = calculateTotalAmount(
                loanOfferThree.requestedAmount,
                loanOfferThree.isInsuranceEnabled,
                loanOfferThree.term
            )
            loanOfferThree.rate = calculateRate(
                loanOfferThree.isSalaryClient,
                loanOfferThree.isInsuranceEnabled
            )
            loanOfferThree.monthlyPayment = calculateMonthlyPayment(
                loanOfferThree.totalAmount,
                loanOfferThree.rate,
                loanOfferThree.term
            )
        }
        run {
            val loanOfferFour = LoanOfferDTO()
            result.add(loanOfferFour)
            loanOfferFour.requestedAmount = loanApplicationRequestDTO.amount
            loanOfferFour.term = loanApplicationRequestDTO.term
            loanOfferFour.isInsuranceEnabled = true
            loanOfferFour.isSalaryClient = true
            loanOfferFour.totalAmount = calculateTotalAmount(
                loanOfferFour.requestedAmount,
                loanOfferFour.isInsuranceEnabled,
                loanOfferFour.term
            )
            loanOfferFour.rate = calculateRate(
                loanOfferFour.isSalaryClient,
                loanOfferFour.isInsuranceEnabled
            )
            loanOfferFour.monthlyPayment = calculateMonthlyPayment(
                loanOfferFour.totalAmount,
                loanOfferFour.rate,
                loanOfferFour.term
            )
        }
        return result
    }

    private fun calculateRate(isSalaryClient: Boolean, isInsuranceEnabled: Boolean): BigDecimal? {
        var res = BigDecimal.valueOf(baseRate.toDouble())
        if (isSalaryClient) {
            res = res.subtract(BigDecimal.ONE)
        }
        if (isInsuranceEnabled) {
            res = res.subtract(BigDecimal.valueOf(5))
        }
        return res
    }

    private fun calculateRate(scoringDataDTO: ScoringDataDTO): BigDecimal {
        var rate = BigDecimal.valueOf(baseRate.toDouble())
        val employmentDTO = scoringDataDTO.employment
        if (employmentDTO.employmentStatus == EmploymentDTO.EmploymentStatusEnum.SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.valueOf(1))
        } else if (employmentDTO.employmentStatus == EmploymentDTO.EmploymentStatusEnum.BUSINESS_OWNER) {
            rate = rate.add(BigDecimal.valueOf(3))
        }
        if (employmentDTO.position == EmploymentDTO.PositionEnum.MIDDLE_MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(2))
        } else if (employmentDTO.position == EmploymentDTO.PositionEnum.TOP_MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(4))
        }
        if (scoringDataDTO.maritalStatus == ScoringDataDTO.MaritalStatusEnum.MARRIED) {
            rate = rate.subtract(BigDecimal.valueOf(3))
        } else if (scoringDataDTO.maritalStatus == ScoringDataDTO.MaritalStatusEnum.DIVORCE) {
            rate = rate.add(BigDecimal.valueOf(1))
        }
        val diffYearBirthDateAndNow = ChronoUnit.YEARS.between(scoringDataDTO.birthdate, LocalDate.now())
        if ((scoringDataDTO.gender == ScoringDataDTO.GenderEnum.FEMALE &&
                    diffYearBirthDateAndNow in 35..60) ||
                    (scoringDataDTO.gender == ScoringDataDTO.GenderEnum.MALE &&
                            diffYearBirthDateAndNow in 30..55)) {
            rate = rate.subtract(BigDecimal.valueOf(3))
        } else if (scoringDataDTO.gender == ScoringDataDTO.GenderEnum.NON_BINARY) {
            rate = rate.add(BigDecimal.valueOf(3))
        }
        if (scoringDataDTO.dependentAmount > 1) {
            rate = rate.add(BigDecimal.valueOf(1))
        }
        return rate
    }

    private fun calculateMonthlyPayment(totalAmount: BigDecimal, rate: BigDecimal, term: Int): BigDecimal {
        val monthlyInterestRate = rate.divide(BigDecimal.valueOf((12 * 100).toLong()), 10, RoundingMode.HALF_EVEN)
        val numerator = monthlyInterestRate.multiply(monthlyInterestRate.add(BigDecimal.ONE).pow(term))
        val denominator = monthlyInterestRate.add(BigDecimal.ONE).pow(term).subtract(BigDecimal.ONE)
        return numerator.divide(denominator, 32, RoundingMode.HALF_EVEN).multiply(totalAmount)
            .setScale(2, RoundingMode.HALF_EVEN)
    }

    private fun validateRefuse(scoringDataDTO: ScoringDataDTO): Boolean {
        val employmentDTO = scoringDataDTO.employment
        if (employmentDTO.employmentStatus.value == EmploymentStatus.UNEMPLOYED.value) {
            return false
        }
        val amount = scoringDataDTO.amount
        val salary = employmentDTO.salary
        if (amount < salary.multiply(BigDecimal.valueOf(20))) {
            return false
        }
        val birthDate = scoringDataDTO.birthdate
        if (ChronoUnit.YEARS.between(birthDate, LocalDate.now()) < 20 ||
            ChronoUnit.YEARS.between(birthDate, LocalDate.now()) > 60
        ) {
            return false
        }
        if (employmentDTO.workExperienceCurrent < 3) {
            return false
        }
        return employmentDTO.workExperienceTotal >= 12
    }

    private fun generatePaymentSchedule(
        totalAmount: BigDecimal, rate: BigDecimal,
        term: Int, monthlyPayment: BigDecimal
    ): List<PaymentScheduleElement> {
        val monthlyInterestRate = rate.divide(BigDecimal.valueOf((12 * 100).toLong()), 10, RoundingMode.HALF_EVEN)
        var remainingDebt = totalAmount
        val paymentSchedule: MutableList<PaymentScheduleElement> = ArrayList()
        for (i in 1..term) {
            val interestPayment = remainingDebt.multiply(monthlyInterestRate).setScale(2, RoundingMode.UP)
            val debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.UP)
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.UP)
            val element = PaymentScheduleElement()
            element.number = i
            element.date = LocalDate.now().plusMonths(i.toLong())
            element.totalPayment = monthlyPayment
            element.interestPayment = interestPayment
            element.debtPayment = debtPayment
            element.remainingDebt = remainingDebt
            paymentSchedule.add(element)
        }
        return paymentSchedule
    }

    private fun calculateTotalAmount(requestedAmount: BigDecimal, isInsuranceEnabled: Boolean, term: Int): BigDecimal? {
        return if (!isInsuranceEnabled) requestedAmount else requestedAmount.add(
            BigDecimal.valueOf(10000L)
                .add(
                    requestedAmount.divide(BigDecimal.valueOf(1000), MathContext.DECIMAL128)
                        .multiply(BigDecimal.valueOf(term.toLong()))
                )
        )
    }
}