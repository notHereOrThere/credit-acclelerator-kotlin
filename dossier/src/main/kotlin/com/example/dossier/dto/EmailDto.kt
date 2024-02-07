package com.example.dossier.dto

data class EmailDto(
    var applicationId: Long? = null,
    var email: String? = null,
    var fio: String? = null,
    var emailText: String? = null
)
