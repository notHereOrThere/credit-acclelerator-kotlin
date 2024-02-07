package com.example.application.api

import com.example.application.service.ApplicationService
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.FeignException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ExtendWith(MockitoExtension::class)
class ApplicationControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var applicationService: ApplicationService

    @InjectMocks
    private lateinit var applicationController: ApplicationController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(applicationController)
            .setControllerAdvice(ExceptionController()) // если у вас есть ControllerAdvice
            .build()
    }

    @Test
    fun testCalculateLoanOffers() {
        // Arrange
        val requestDTO = LoanApplicationRequestDTO()
        val loanOfferDTOs = listOf(LoanOfferDTO())

        whenever(applicationService.calculateLoanOffers(requestDTO)).thenReturn(loanOfferDTOs)

        // Act & Assert
        mockMvc.perform(post("/application")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestDTO)))
            .andExpect(status().isOk)
            .andExpect(content().json(asJsonString(loanOfferDTOs)))

        verify(applicationService).calculateLoanOffers(requestDTO)
    }

    @Test
    fun testChooseOffer() {
        // Arrange
        val loanOfferDTO = LoanOfferDTO()

        doNothing().whenever(applicationService).chooseOffer(loanOfferDTO)

        mockMvc.perform(put("/application/offer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(loanOfferDTO)))
            .andExpect(status().isNoContent)

        verify(applicationService).chooseOffer(loanOfferDTO)
    }

    companion object {
        fun asJsonString(obj: Any): String =
            jacksonObjectMapper().writeValueAsString(obj)

        class ExceptionController {
            @ExceptionHandler(FeignException::class)
            @ResponseStatus(HttpStatus.BAD_REQUEST)
            fun handleException(e: Exception): ResponseEntity<String> =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка ввода: ${e.message}")
        }
    }
}
