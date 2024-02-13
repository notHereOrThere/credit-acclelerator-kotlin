package com.example.gateway.api

import com.example.credit.application.api.AuditEventsApi
import com.example.credit.application.api.DealApi
import com.example.credit.application.model.AuditDto
import com.example.credit.application.model.AuditRequestDto
import com.example.credit.application.model.FinishRegistrationRequestDTO
import com.example.credit.application.model.SesDto
import com.example.gateway.feign.AuditFeignClient
import com.example.gateway.feign.DealFeignClient
import feign.FeignException
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuditController (private val auditFeignClient: AuditFeignClient) : AuditEventsApi {

    override fun getAuditEventByUUID(uuid : String): ResponseEntity<AuditDto> {
        return ResponseEntity.ok(auditFeignClient.getAuditEventByUUID(uuid))
    }

    override fun queryAuditEventsByServiceAndType(auditRequestDto: AuditRequestDto): ResponseEntity<List<AuditDto>> {
        return ResponseEntity.ok(auditFeignClient.queryAuditEventsByServiceAndType(auditRequestDto))
    }

}
