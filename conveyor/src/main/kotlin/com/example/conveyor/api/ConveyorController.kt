package com.example.conveyor.api

import com.example.conveyor.model.exception.UserException
import com.example.conveyor.service.ConveyorService
import com.example.credit.application.api.ConveyorApi
import com.example.credit.application.model.CreditDTO
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.ScoringDataDTO
import io.swagger.annotations.Api
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "Api для работы МС conveyor")
@RequiredArgsConstructor
class ConveyorController(private val conveyorService: ConveyorService) : ConveyorApi {

    override fun calculateLoanOffers(loanApplicationRequestDTO: LoanApplicationRequestDTO): ResponseEntity<List<LoanOfferDTO>> {
        val loanOffers: List<LoanOfferDTO> = conveyorService.offers(loanApplicationRequestDTO)
        return ResponseEntity.ok(loanOffers)
    }

    override fun performLoanCalculation(scoringDataDTO: ScoringDataDTO): ResponseEntity<CreditDTO> {
        val creditDTO: CreditDTO = conveyorService.calculation(scoringDataDTO)
        return ResponseEntity.ok(creditDTO)
    }

    @ExceptionHandler(UserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            e.message?.substring(e.message?.indexOf("Прескоринг") ?: 0) ?: "Ошибка")
    }
}