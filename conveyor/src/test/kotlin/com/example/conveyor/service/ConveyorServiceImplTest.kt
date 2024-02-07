package com.example.conveyor.service

import com.example.conveyor.model.exception.UserException
import com.example.credit.application.model.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class ConveyorServiceImplTest {
    @InjectMocks
    private lateinit var conveyorService: ConveyorServiceImpl
    @BeforeEach
    fun init() {
        ReflectionTestUtils.setField(conveyorService!!, "baseRate", "15")
    }

    @Test
    fun testOffersWithoutErrors() {
        val dto = LoanApplicationRequestDTO().apply {
            amount = BigDecimal.valueOf(300000)
            term = 15
            firstName = "Ivan"
            lastName = "Ivanov"
            middleName = "Ivanovich"
            email = "ivan@example.com"
            birthdate = LocalDate.parse("2000-01-01")
            passportSeries = "1234"
            passportNumber = "123456"
        }

        val result = conveyorService.offers(dto)

        assertEquals(22057.94, result[0].monthlyPayment.toDouble(), 0.01)
        assertEquals(21917.16, result[1].monthlyPayment.toDouble(), 0.01)
        assertEquals(22391.5,  result[2].monthlyPayment.toDouble(), 0.01)
        assertEquals(22246.59, result[3].monthlyPayment.toDouble(), 0.01)
    }


    @Test
    fun testOffersWithErrors() {
        val dto = LoanApplicationRequestDTO()
        dto.amount = BigDecimal.valueOf(3000)
        dto.term = 5
        dto.firstName = "I"
        dto.lastName = "Ivanov"
        dto.middleName = "Ivanovich"
        dto.email = "ivanexample.com"
        dto.birthdate = LocalDate.parse("2020-01-01")
        dto.passportSeries = "12341"
        dto.passportNumber = "123456"
        ReflectionTestUtils.setField(conveyorService!!, "baseRate", "15")
        Assertions.assertThrows(
            UserException::class.java
        ) { conveyorService.offers(dto) }
    }

    @Test
    fun testCalculationWithoutRefuse() {
        val dto = ScoringDataDTO()
        dto.amount = BigDecimal.valueOf(300000)
        dto.term = 15
        dto.firstName = "Ivan"
        dto.lastName = "Ivanov"
        dto.middleName = "Ivanovich"
        dto.birthdate = LocalDate.parse("2000-01-01")
        dto.passportSeries = "1234"
        dto.passportNumber = "123456"
        dto.passportIssueDate = LocalDate.parse("2022-01-31")
        dto.passportIssueBranch = "ОВД района Иваново"
        dto.dependentAmount = 2
        dto.account = "408178xxxxxxxxxx1234"
        dto.isInsuranceEnabled = true
        dto.isSalaryClient = true
        dto.maritalStatus = ScoringDataDTO.MaritalStatusEnum.MARRIED
        dto.gender = ScoringDataDTO.GenderEnum.MALE
        val employmentDTO = EmploymentDTO()
        employmentDTO.employerINN = "123456789"
        employmentDTO.salary = BigDecimal.valueOf(45000)
        employmentDTO.workExperienceTotal = 120
        employmentDTO.workExperienceCurrent = 60
        employmentDTO.position = EmploymentDTO.PositionEnum.MIDDLE_MANAGER
        employmentDTO.employmentStatus = EmploymentDTO.EmploymentStatusEnum.EMPLOYED
        dto.employment = employmentDTO
        val creditDTO = conveyorService.calculation(dto)
        assertEquals(21497.88, creditDTO.monthlyPayment.toDouble())
        assertEquals(322468.2, creditDTO.psk.toDouble())
    }
}