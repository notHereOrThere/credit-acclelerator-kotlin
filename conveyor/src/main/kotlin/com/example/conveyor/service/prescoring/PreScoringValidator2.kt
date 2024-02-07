package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO
import java.math.BigDecimal

class PreScoringValidator2 : PreScoring{

    override fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO): ValidatorResult {
        val amount:BigDecimal = loanApplicationRequestDTO.amount
        if (amount.compareTo(BigDecimal("10000")) == -1) {
            return ValidatorResult(
                2,
                "Сумма кредита - действительно число, большее или равное 10000.",
                false
            )
        }
        return ValidatorResult(isValid = true)
    }
}