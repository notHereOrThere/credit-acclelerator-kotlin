package com.example.dossier.dto

import com.example.deal.entity.enums.Status
import com.example.dossier.dto.inner.PaymentSchedule
import java.math.BigDecimal

class Credit {
    var creditId: Long? = null
    var amount: BigDecimal? = null
    var applications: List<Application> = ArrayList()
    var term: Int? = null
    var monthlyPayment: BigDecimal? = null
    var rate: BigDecimal? = null
    var psk: BigDecimal? = null
    var paymentSchedule: List<PaymentSchedule> = ArrayList()
    var isInsuranceEnabled: Boolean? = null
    var isSalaryClient: Boolean? = null
    var creditStatus: Status? = null
}