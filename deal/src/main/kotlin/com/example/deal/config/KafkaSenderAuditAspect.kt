package com.example.deal.config

import com.example.credit.application.model.LoanOfferDTO
import com.example.credit.application.model.SesDto
import com.example.deal.dto.AuditDto
import com.example.deal.dto.enums.Type
import com.example.deal.entity.Application
import com.example.deal.exception.UserException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.*
import javax.persistence.EntityNotFoundException


@Aspect
@Component
class KafkaSenderAuditAspect (@Autowired private val kafkaTemplate: KafkaTemplate<String, Any>) {

    @Value("\${topic.audit}")
    private val audit: String? = null

    @Around("@annotation(sendToAudit)")
    fun sendKafkaMessage(joinPoint: ProceedingJoinPoint, sendToAudit: SendToAudit) : Any? {

        val auditDto = AuditDto(
            uuid = UUID.randomUUID().toString(),
            type = sendToAudit.type.value,
            service = sendToAudit.service.value,
            message = ""
        )

        val result : Any

        try {
            if (joinPoint.signature.name == "chooseLoanOffer") {
                joinPoint.proceed()
                val loanOfferDTO = joinPoint.args[0]
                if (loanOfferDTO is LoanOfferDTO) {
                    val template:StringBuilder  = StringBuilder()
                    template.append("Дата: ").append(Date()).append("\n")
                    template.append("Сообщение из метода: ").append(joinPoint.signature.name).append("\n")
                    template.append("Выбрано предложение с ID заявления: ").append(loanOfferDTO.applicationId).append("\n")
                    auditDto.message=template.toString()
                    if (audit != null) {
                        kafkaTemplate.send(audit, auditDto)
                    }
                }
                return null
            }

            if (joinPoint.signature.name == "sendDocuments" || joinPoint.signature.name == "codeDocuments") {
                joinPoint.proceed()
                val applicationId = joinPoint.args[0]
                if (applicationId is Long) {
                    val template:StringBuilder  = StringBuilder()
                    template.append("Дата: ").append(Date()).append("\n")
                    template.append("Сообщение из метода: ").append(joinPoint.signature.name).append("\n")
                    template.append("Выбрано предложение с ID заявления: ").append(applicationId).append("\n")
                    auditDto.message=template.toString()
                    if (audit != null) {
                        kafkaTemplate.send(audit, auditDto)
                    }
                }
                return null
            }

            result = joinPoint.proceed()

            if (result is Application) {
                val template:StringBuilder  = StringBuilder()
                template.append("Дата: ").append(Date()).append("\n")
                template.append("Сообщение из метода: ").append(joinPoint.signature.name).append("\n")
                template.append("Запрос заявки с ID: ").append(result.applicationId).append("\n")
                auditDto.message=template.toString()

            }

            if (result is List<*>) {
                val temp = result[0]
                if (temp is LoanOfferDTO) {
                    val template:StringBuilder  = StringBuilder()
                    template.append("Дата: ").append(Date()).append("\n")
                    template.append("Сообщение из метода: ").append(joinPoint.signature.name).append("\n")
                    template.append("ID заявления: ").append(temp.applicationId).append("\n")
                    template.append("Запрашиваемая сумма: ").append(temp.requestedAmount).append("\n")
                    auditDto.message=template.toString()

                }
                if (temp is Application) {
                    val template:StringBuilder  = StringBuilder()
                    template.append("Дата: ").append(Date()).append("\n")
                    template.append("Сообщение из метода: ").append(joinPoint.signature.name).append("\n")
                    auditDto.message=template.toString()
                }
            }

            if (audit != null) {
                kafkaTemplate.send(audit, auditDto)
            }
            return result
        } catch (e: UserException) {
            auditDto.type = Type.FAILURE.value
            val template:StringBuilder  = StringBuilder()
            template.append("Дата: ").append(Date()).append(" Текст ошибки: ").append(e.message.toString())
            auditDto.message=template.toString()

            if (audit != null) {
                kafkaTemplate.send(audit, auditDto)
            }
            throw UserException(e.message)
        }

    }
}
