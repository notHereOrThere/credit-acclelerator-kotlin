package com.example.audit

import com.example.audit.dto.AuditEventDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class AuditApplication

fun main(args: Array<String>) {
    runApplication<AuditApplication>(*args)
}
