package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO

class PreScoringValidator1 : PreScoring {
    override fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO): ValidatorResult {
        val firstName = loanApplicationRequestDTO.firstName
        val lastName = loanApplicationRequestDTO.lastName
        val middleName = loanApplicationRequestDTO.middleName

        if (!checkName(firstName) || !checkName(lastName) || (middleName.isNotBlank() && !checkName(middleName))) {
            return ValidatorResult(
                codeErr = 1,
                textErr = "Имя, Фамилия - от 2 до 30 латинских букв. Отчество, при наличии - от 2 до 30 латинских букв.",
                isValid = false
            )
        }
        return ValidatorResult(isValid = true)
    }

    private fun checkName(str: String): Boolean {
        return str.matches("[a-zA-Z]{2,30}".toRegex())
    }
}
