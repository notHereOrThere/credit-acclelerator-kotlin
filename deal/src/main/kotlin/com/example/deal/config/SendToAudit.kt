package com.example.deal.config

import com.example.deal.dto.enums.Service
import com.example.deal.dto.enums.Type

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SendToAudit(val service : Service, val type : Type)
