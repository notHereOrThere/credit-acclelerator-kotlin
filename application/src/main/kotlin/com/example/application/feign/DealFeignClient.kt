package com.example.application.feign

import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "deal", url = "\${deal.url}")
interface DealFeignClient {

    @PostMapping(value = ["/deal/application"], produces = ["application/json"])
    fun calculateLoanOffers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO>

    @PutMapping(value = ["/deal/offer"], produces = ["application/json"])
    fun performLoanCalculation(@RequestBody scoringDataDTO: LoanOfferDTO)
}
