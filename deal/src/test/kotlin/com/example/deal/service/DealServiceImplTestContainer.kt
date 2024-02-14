package com.example.deal.service

import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.deal.DealApplication
import com.example.deal.entity.Application
import com.example.deal.exception.UserException
import com.example.deal.repository.ApplicationRepository
import feign.FeignException
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import java.math.BigDecimal
import java.time.LocalDate


@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["application-test.properties"], classes = [DealApplication::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
@ContextConfiguration(initializers = [DealServiceImplTestContainer.Initializer::class])
class DealServiceImplTestContainer {

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgresqlContainer.jdbcUrl,
                "spring.datasource.username=" + postgresqlContainer.username,
                "spring.datasource.password=" + postgresqlContainer.password
            ).applyTo(configurableApplicationContext.environment)
        }
    }

    companion object {
        @Container
        val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:13.2").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
        }.also { it.start() }

        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
            registry.add("spring.liquibase.change-log") { "classpath:/db-test/changelog/db.changelog.yaml" }
        }
    }

    @Autowired
    private lateinit var dealService: DealService
    @Autowired
    private lateinit var applicationRepository: ApplicationRepository

    @Test
    fun `test prescoring fail`() {
        val loanApplicationRequestDTO = LoanApplicationRequestDTO()
        with(loanApplicationRequestDTO) {
            amount = BigDecimal(300000)
            term = 15
            firstName = "Ivan"
            lastName = "Ivanov"
            middleName = "Ivanovich"
            email = "ivan@example.com"
            birthdate = LocalDate.of(2022, 1, 1)
            passportSeries = "1234"
            passportNumber = "123456"
        }
        assertThrows<UserException> { dealService.calculateLoanConditions(loanApplicationRequestDTO) }
    }

    @Test
    fun `test prescoring success`() {
        val loanApplicationRequestDTO = LoanApplicationRequestDTO()
        with(loanApplicationRequestDTO) {
            amount = BigDecimal(300000)
            term = 15
            firstName = "Ivan"
            lastName = "Ivanov"
            middleName = "Ivanovich"
            email = "ivan@example.com"
            birthdate = LocalDate.of(2002, 1, 1)
            passportSeries = "1234"
            passportNumber = "123456"
        }
        val offers = dealService.calculateLoanConditions(loanApplicationRequestDTO)

        assertDoesNotThrow {  dealService.chooseLoanOffer(offers[3]) }

    }
}