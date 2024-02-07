package com.example.gateway.feign

import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "gateway-application", url = "\${application.url}")
interface ApplicationFeignClient {
    @PostMapping(value = ["/application"], produces = ["application/json"])
    fun calculateLoanOffers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO?): List<LoanOfferDTO?>?

    @PutMapping(value = ["/application/offer"], produces = ["application/json"])
    fun chooseOffer(@RequestBody scoringDataDTO: LoanOfferDTO?)
}