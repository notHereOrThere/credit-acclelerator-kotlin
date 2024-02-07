package com.example.dossier.dto.inner

import java.io.Serializable
import java.time.LocalDate

class Passport : Serializable {
    private var passportId: Long? = null
    private var passportNum: String? = null
    private var passportSer: String? = null
    private var passportIssueDate: LocalDate? = null
    private var passportIssueBranch: String? = null
}
