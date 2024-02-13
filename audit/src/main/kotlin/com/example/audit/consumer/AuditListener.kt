package com.example.audit.consumer

import com.example.audit.dto.AuditEventDto
import com.example.audit.service.AuditService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.MessagingException
import org.springframework.stereotype.Service
import java.lang.String

@Service
class AuditListener (private val auditService: AuditService){

    @KafkaListener(topics = ["\${topic.audit}"], groupId = "\${spring.kafka.consumer.group-id}")
    @Throws(
        MessagingException::class
    )
    fun sendApplicationDenied(event: AuditEventDto) {
        LOGGER.info(String.format("Audit event received in audit service -> %s", event.toString()))
        auditService.persistToRedis(event)
        LOGGER.info("AuditDto event executed in audit service")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuditListener::class.java)
    }
}
