package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO

class PreScoringValidator6 : PreScoring {
    override fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO): ValidatorResult {
        val ser = loanApplicationRequestDTO.passportSeries
        val num = loanApplicationRequestDTO.passportNumber

        val regexSer = "\\d{4}"
        val regexNum = "\\d{6}"

        if (!(ser.matches(regexSer.toRegex()) && num.matches(regexNum.toRegex()))) {
            return ValidatorResult(
                codeErr = 6,
                textErr = "Серия паспорта - 4 цифры, номер паспорта - 6 цифр.",
                isValid = false
            )
        }
        return ValidatorResult(isValid = true)
    }
}
