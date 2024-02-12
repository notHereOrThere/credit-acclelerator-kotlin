package com.example.gateway.feign

import com.example.credit.application.model.AuditDto
import com.example.credit.application.model.AuditRequestDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(name = "gateway-audit", url = "\${audit.url}")
interface AuditFeignClient {

    @PostMapping(value = ["/audit-events/query"], produces = ["application/json"])
    fun queryAuditEventsByServiceAndType(@RequestBody auditRequestDto: AuditRequestDto): List<AuditDto>

    @GetMapping(value = ["/audit-events/{uuid}"], produces = ["application/json"])
    fun getAuditEventByUUID(@PathVariable("uuid") uuid: String) : AuditDto


}