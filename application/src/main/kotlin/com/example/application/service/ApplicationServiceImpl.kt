package com.example.application.service

import com.example.application.feign.DealFeignClient
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import org.springframework.stereotype.Service

@Service
class ApplicationServiceImpl(private val dealFeignClient: DealFeignClient) : ApplicationService{
    override fun calculateLoanOffers(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        return dealFeignClient.calculateLoanOffers(loanApplicationRequestDTO)
    }

    override fun chooseOffer(loanOfferDTO: LoanOfferDTO) {
        dealFeignClient.performLoanCalculation(loanOfferDTO)
    }
}