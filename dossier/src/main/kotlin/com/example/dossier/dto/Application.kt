package com.example.dossier.dto

import com.example.deal.entity.enums.ApplicationStatus
import com.example.dossier.dto.inner.LoanOffer
import com.example.dossier.dto.inner.StatusHistory
import java.util.*

class Application {
    var applicationId: Long? = null
    var client: Client? = null
    var credit: Credit? = null
    var status: ApplicationStatus? = null
    var creationDate: Date? = null
    var appliedOffer: LoanOffer? = null
    var signDate: Date? = null
    var sesCode: String? = null
    var statusHistory: List<StatusHistory> = ArrayList()
}