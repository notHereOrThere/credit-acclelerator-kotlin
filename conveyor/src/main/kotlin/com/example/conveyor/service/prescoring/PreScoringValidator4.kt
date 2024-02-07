package com.example.conveyor.service.prescoring

import com.example.credit.application.model.LoanApplicationRequestDTO
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class PreScoringValidator4 : PreScoring {
    override fun preScore(loanApplicationRequestDTO: LoanApplicationRequestDTO): ValidatorResult {
        val birthdate = loanApplicationRequestDTO.birthdate
        if (ChronoUnit.YEARS.between(birthdate, LocalDate.now()) < 18) {
            return ValidatorResult(
                codeErr = 4,
                textErr = "Дата рождения - число в формате гггг-мм-дд, не позднее 18 лет с текущего дня.",
                isValid = false
            )
        }
        return ValidatorResult(isValid = true)
    }
}
