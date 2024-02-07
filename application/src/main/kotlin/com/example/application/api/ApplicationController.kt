package com.example.application.api

import com.example.application.service.ApplicationService
import com.example.credit.application.api.ApplicationApi
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ApplicationController(
    private val applicationService: ApplicationService
) : ApplicationApi {

    override fun calculateLoanOffers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): ResponseEntity<List<LoanOfferDTO>> {
        val loanOfferDTOs = applicationService.calculateLoanOffers(loanApplicationRequestDTO)
        return ResponseEntity.ok(loanOfferDTOs)
    }

    override fun chooseOffer(@RequestBody loanOfferDTO: LoanOfferDTO): ResponseEntity<Void> {
        applicationService.chooseOffer(loanOfferDTO)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(FeignException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: Exception): ResponseEntity<String> {
        val message = e.message?.substring(e.message?.indexOf("Прескоринг") ?: 0) ?: "Ошибка"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }
}
