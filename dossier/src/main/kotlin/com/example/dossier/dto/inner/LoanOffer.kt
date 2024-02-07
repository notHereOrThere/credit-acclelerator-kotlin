package com.example.dossier.dto.inner

import com.example.dossier.dto.Application
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.math.BigDecimal

class LoanOffer : Serializable {
    private var appliedOfferId: Long? = null

    @JsonIgnore
    private var application: Application? = null
    private var requestedAmount: BigDecimal? = null
    private var totalAmount: BigDecimal? = null
    private var term: Int? = null
    private var monthlyPayment: BigDecimal? = null
    private var rate: BigDecimal? = null
    private var isInsuranceEnabled: Boolean? = null
    private var isSalaryClient: Boolean? = null
}
