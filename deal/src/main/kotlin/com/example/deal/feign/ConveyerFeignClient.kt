package com.example.deal.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.example.credit.application.model.CreditDTO
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.ScoringDataDTO
import org.springframework.stereotype.Service


@FeignClient(name = "deal", url = "\${conveyer.url}")
@Service
interface ConveyerFeignClient {
    @PostMapping(value = ["/conveyor/offers"], produces = ["application/json"])
    fun calculateLoanOffers(@RequestBody loanApplicationRequestDTO: LoanApplicationRequestDTO?): List<LoanOfferDTO?>?

    @PostMapping(value = ["/conveyor/calculation"], produces = ["application/json"])
    fun performLoanCalculation(@RequestBody scoringDataDTO: ScoringDataDTO?): CreditDTO?
}
