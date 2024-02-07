package com.example.conveyor.service

import com.example.credit.application.model.CreditDTO
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.ScoringDataDTO

interface ConveyorService {

    fun offers(loanApplicationRequestDTO: LoanApplicationRequestDTO) : List<LoanOfferDTO>

    fun calculation(scoringDataDTO: ScoringDataDTO) : CreditDTO
}