package com.example.dossier.consumer

import com.example.dossier.dto.Application
import com.example.dossier.dto.EmailDto
import com.example.dossier.feign.DossierFeignClient
import com.example.dossier.mail.EmailService
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.io.IOException
import java.lang.String
import javax.mail.MessagingException

@Service
@RequiredArgsConstructor
class DossierListener (private val emailService: EmailService,
                       private val dossierFeignClient: DossierFeignClient
){

    @KafkaListener(topics = ["\${topic.application-denied}"], groupId = "\${spring.kafka.consumer.group-id}")
    @Throws(
        MessagingException::class
    )
    fun sendApplicationDenied(event: EmailDto) {
        LOGGER.info(String.format("Email event received in dossier service -> %s", event.toString()))
        emailService.sendApplicationDenied(event)
        LOGGER.info("Email event executed in dossier service")
    }

    @KafkaListener(topics = ["\${topic.finish-registration}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun sendFinishRegistration(event: EmailDto) {
        LOGGER.info(String.format("Email event received in dossier service -> %s", event.toString()))
        emailService.sendFinishRegistration(event)
        LOGGER.info("Email event executed in dossier service")
    }

    @KafkaListener(topics = ["\${topic.create-documents}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun sendCreateDocuments(event: EmailDto) {
        LOGGER.info(String.format("Email event received in dossier service -> %s", event.toString()))
        emailService.sendCreateDocuments(event)
        LOGGER.info("Email event executed in dossier service")
    }

    @KafkaListener(topics = ["\${topic.send-documents}"], groupId = "\${spring.kafka.consumer.group-id}")
    @Throws(
        MessagingException::class,
        IOException::class
    )
    fun sendSendDocuments(event: Application) {
        LOGGER.info(String.format("Application event received in dossier service -> %s", event.toString()))
        emailService.sendSendDocuments(event)
        dossierFeignClient.signDocuments(event.applicationId)
        LOGGER.info("Application event executed in dossier service")
    }

    @KafkaListener(topics = ["\${topic.send-ses}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun sendSendSes(event: EmailDto) {
        LOGGER.info(String.format("Email event received in dossier service -> %s", event.toString()))
        emailService.sendSendSes(event)
        LOGGER.info("Email event executed in dossier service")
    }

    @KafkaListener(topics = ["\${topic.credit-issued}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun sendCreditIssued(event: EmailDto) {
        LOGGER.info(String.format("Email event received in dossier service -> %s", event.toString()))
        emailService.sendCreditIssued(event)
        LOGGER.info("Email event executed in dossier service")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DossierListener::class.java)
    }
}
