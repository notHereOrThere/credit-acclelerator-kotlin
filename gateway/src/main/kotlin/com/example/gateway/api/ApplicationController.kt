package com.example.gateway.api

import com.example.credit.application.api.ApplicationApi
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.gateway.feign.ApplicationFeignClient
import feign.FeignException
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ApplicationController  (private var applicationFeignClient: ApplicationFeignClient) : ApplicationApi {
    override fun calculateLoanOffers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): ResponseEntity<List<LoanOfferDTO>> {
        return ResponseEntity.ok(
            applicationFeignClient.calculateLoanOffers(loanApplicationRequestDTO)
                ?.filterNotNull()
        )
    }

    override fun chooseOffer(@RequestBody loanOfferDTO: LoanOfferDTO): ResponseEntity<Void> {
        applicationFeignClient!!.chooseOffer(loanOfferDTO)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(FeignException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            e.message?.substring(e.message?.indexOf("Прескоринг") ?: 0) ?: "Ошибка")
    }
}
