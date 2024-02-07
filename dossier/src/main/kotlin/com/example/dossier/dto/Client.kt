package com.example.dossier.dto

import com.example.deal.entity.enums.Gender
import com.example.deal.entity.enums.MaritalStatus
import com.example.dossier.dto.inner.Employment
import com.example.dossier.dto.inner.Passport
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

class Client {
    var clientId: Long? = null
    var lastName: String? = null
    var firstName: String? = null
    var middleName: String? = null
    @JsonFormat(pattern = "yyyy-MM-dd")
    var birthdate: LocalDate? = null
    var email: String? = null
    var gender: Gender? = null
    var martialStatus: MaritalStatus? = null
    var dependentAmount: Int? = null
    var passport: Passport? = null
    var employment: Employment? = null
    var applications: List<Application> = ArrayList()
}