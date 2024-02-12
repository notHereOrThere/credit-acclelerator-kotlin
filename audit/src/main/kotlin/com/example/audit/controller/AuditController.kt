package com.example.audit.controller

import com.example.audit.service.AuditService
import com.example.credit.application.api.AuditEventsApi
import com.example.credit.application.model.AuditDto
import com.example.credit.application.model.AuditRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuditController (private val auditService: AuditService) : AuditEventsApi {

    override fun getAuditEventByUUID(uuid: String): ResponseEntity<AuditDto> {
        return ResponseEntity.ok(auditService.getAuditEventByUUID(uuid))
    }

    override fun queryAuditEventsByServiceAndType(auditRequestDto: AuditRequestDto): ResponseEntity<List<AuditDto?>?>? {
        return ResponseEntity.ok(auditService.findAllByTypeAndService(
            auditRequestDto.type.value, auditRequestDto.service.value
        ))
    }

}