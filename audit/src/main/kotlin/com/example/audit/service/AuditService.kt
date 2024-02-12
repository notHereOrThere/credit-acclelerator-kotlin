package com.example.audit.service

import com.example.audit.dto.AuditEventDto
import com.example.credit.application.model.AuditDto

interface AuditService {

    fun persistToRedis(dto: AuditEventDto)
    fun getAuditEventByUUID(uuid: String) : AuditDto
    fun findAllByTypeAndService(type: String?, service: String?): List<AuditDto>
}