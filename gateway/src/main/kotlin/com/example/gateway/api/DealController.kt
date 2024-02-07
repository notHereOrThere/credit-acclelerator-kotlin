package com.example.gateway.api

import com.example.credit.application.api.DealApi
import com.example.credit.application.model.FinishRegistrationRequestDTO
import com.example.credit.application.model.SesDto
import com.example.gateway.feign.DealFeignClient
import feign.FeignException
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class DealController (private val dealFeignClient: DealFeignClient) : DealApi {

    override fun confirmSignDocuments(
        @PathVariable applicationId: Long,
        @RequestBody sesDto: SesDto
    ): ResponseEntity<Void> {
        dealFeignClient!!.confirmSignDocuments(applicationId, sesDto)
        return ResponseEntity.noContent().build()
    }

    override fun finishRegistrationAndCalculateLoan(
        @PathVariable applicationId: Long,
        @RequestBody finishRegistrationRequestDTO: FinishRegistrationRequestDTO
    ): ResponseEntity<Void> {
        dealFeignClient!!.finishRegistrationAndCalculateLoan(applicationId, finishRegistrationRequestDTO)
        return ResponseEntity.noContent().build()
    }

    override fun sendDocuments(@PathVariable applicationId: Long): ResponseEntity<Void> {
        dealFeignClient!!.sendDocuments(applicationId)
        return ResponseEntity.noContent().build()
    }

    override fun signDocuments(@PathVariable applicationId: Long): ResponseEntity<Void> {
        dealFeignClient!!.signDocuments(applicationId)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(FeignException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            e.message?.substring(e.message?.indexOf("Прескоринг") ?: 0) ?: "Ошибка")
    }
}
