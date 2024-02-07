package com.example.deal.api

import com.example.credit.application.api.DealApi
import com.example.credit.application.model.FinishRegistrationRequestDTO
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.SesDto
import com.example.deal.entity.Application
import com.example.deal.exception.UserException
import com.example.deal.service.DealService
import feign.FeignException
import io.swagger.annotations.Api
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "Api для работы МС deal")
@RestController
@RequiredArgsConstructor
class DealController (private val dealService: DealService): DealApi {


    //    вызов из application
    override fun calculateLoanConditions(loanApplicationRequestDTO: LoanApplicationRequestDTO): ResponseEntity<List<LoanOfferDTO>> {
        val loanOfferDTOs = dealService.calculateLoanConditions(loanApplicationRequestDTO)
        return ResponseEntity.ok(loanOfferDTOs as List<LoanOfferDTO>)
    }

    //    вызов из application
    override fun chooseLoanOffer(loanOfferDTO: LoanOfferDTO): ResponseEntity<Void> {
        dealService!!.chooseLoanOffer(loanOfferDTO)
        return ResponseEntity.noContent().build()
    }

    override fun finishRegistrationAndCalculateLoan(
        applicationId: Long,
        finishRegistrationRequestDTO: FinishRegistrationRequestDTO
    ): ResponseEntity<Void> {
        dealService!!.finishRegistrationAndCalculateLoan(applicationId, finishRegistrationRequestDTO)
        return ResponseEntity.noContent().build()
    }

    override fun sendDocuments(@PathVariable applicationId: Long): ResponseEntity<Void> {
        dealService!!.sendDocuments(applicationId)
        return ResponseEntity.noContent().build()
    }

    override fun signDocuments(@PathVariable applicationId: Long): ResponseEntity<Void> {
        dealService!!.signDocuments(applicationId)
        return ResponseEntity.noContent().build()
    }

    override fun confirmSignDocuments(
        @PathVariable applicationId: Long,
        @RequestBody sesDto: SesDto
    ): ResponseEntity<Void> {
        dealService!!.codeDocuments(applicationId, sesDto)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(UserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            e.message?.substring(e.message?.indexOf("Прескоринг") ?: 0) ?: "Ошибка")
    }
}