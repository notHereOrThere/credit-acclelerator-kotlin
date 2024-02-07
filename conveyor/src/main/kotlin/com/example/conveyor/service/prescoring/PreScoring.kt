package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO

interface PreScoring {

    fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO) : ValidatorResult
}