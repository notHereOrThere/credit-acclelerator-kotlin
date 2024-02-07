package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO

class PreScoringValidator5 : PreScoring {
    override fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO): ValidatorResult {
        val email = loanApplicationRequestDTO.email
        if (!checkEmail(email)) {
            return ValidatorResult(
                codeErr = 5,
                textErr = "Email адрес - строка, подходящая под паттерн [\\w\\.]{2,50}@[\\w\\.]{2,20}",
                isValid = false
            )
        }
        return ValidatorResult(isValid = true)
    }

    private fun checkEmail(str: String): Boolean {
        return str.matches("[\\w\\.]{2,50}@[\\w\\.]{2,20}".toRegex())
    }
}
