package com.example.deal.dto

import java.util.*

data class AuditDto (
    val uuid: String,
    var type: String,
    var service: String,
    var message: String
)
