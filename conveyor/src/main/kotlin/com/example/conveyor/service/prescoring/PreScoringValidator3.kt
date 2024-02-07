package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO

class PreScoringValidator3 : PreScoring {
    override fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO): ValidatorResult {
        val term = loanApplicationRequestDTO.term
        if (term < 6) {
            return ValidatorResult(
                codeErr = 3,
                textErr = "Срок кредита - целое число, большее или равное 6",
                isValid = false
            )
        }
        return ValidatorResult(isValid = true)
    }
}
