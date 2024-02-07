package com.example.application.service

import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO

interface ApplicationService {

    fun calculateLoanOffers(loanApplicationRequestDTO: LoanApplicationRequestDTO) : List<LoanOfferDTO>

    fun chooseOffer(loanOfferDTO: LoanOfferDTO)
}