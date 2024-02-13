package com.example.deal.service

import com.example.credit.application.model.FinishRegistrationRequestDTO
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.SesDto
import com.example.deal.entity.Application

interface DealService {
    fun calculateLoanConditions(loanApplicationRequestDTO: LoanApplicationRequestDTO?): List<LoanOfferDTO?>
    fun chooseLoanOffer(loanOfferDTO: LoanOfferDTO?)
    fun finishRegistrationAndCalculateLoan(
        applicationId: Long?,
        finishRegistrationRequestDTO: FinishRegistrationRequestDTO?
    )

    fun sendDocuments(applicationId: Long?)
    fun signDocuments(applicationId: Long?)
    fun codeDocuments(applicationId: Long?, sesDto: SesDto?)
    fun fetchAllApplications(): List<Application?>

    fun getApplicationById(applicationId: Long?): Application?
}
