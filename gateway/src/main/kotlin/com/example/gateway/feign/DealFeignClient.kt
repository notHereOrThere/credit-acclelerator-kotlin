package com.example.gateway.feign

import com.example.credit.application.model.FinishRegistrationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.SesDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "gateway-deal", url = "\${deal.url}")
interface DealFeignClient {
    @PostMapping(value = ["/deal/document/{applicationId}/code"], produces = ["application/json"])
    fun confirmSignDocuments(@PathVariable applicationId: Long?, @RequestBody sesDto: SesDto?): List<LoanOfferDTO?>?

    @PutMapping(value = ["/deal/calculate/{applicationId}"], produces = ["application/json"])
    fun finishRegistrationAndCalculateLoan(
        @PathVariable applicationId: Long?,
        @RequestBody finishRegistrationRequestDTO: FinishRegistrationRequestDTO?
    )

    @PostMapping(value = ["/deal/document/{applicationId}/send"], produces = ["application/json"])
    fun sendDocuments(@PathVariable applicationId: Long?)

    @PostMapping(value = ["/deal/document/{applicationId}/sign"], produces = ["application/json"])
    fun signDocuments(@PathVariable applicationId: Long?)
}
