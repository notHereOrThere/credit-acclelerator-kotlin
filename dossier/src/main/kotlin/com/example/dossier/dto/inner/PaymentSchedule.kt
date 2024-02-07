package com.example.dossier.dto.inner

import com.example.dossier.dto.Credit
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

class PaymentSchedule : Serializable {
    var paymentScheduleId: Long? = null
    @JsonIgnore
    var credit: Credit? = null
    var number: Int? = null
    var date: LocalDate? = null
    var totalPayment: BigDecimal? = null
    var interestPayment: BigDecimal? = null
    var debtPayment: BigDecimal? = null
    var remainingDebt: BigDecimal? = null
}
