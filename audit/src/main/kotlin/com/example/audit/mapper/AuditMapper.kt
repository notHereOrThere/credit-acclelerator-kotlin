package com.example.audit.mapper

import com.example.audit.dto.AuditEventDto
import com.example.audit.dto.AuditEntity
import com.example.credit.application.model.AuditDto
import com.example.deal.entity.enums.Service
import com.example.deal.entity.enums.Type
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AuditMapper {

    fun eventDtoToEntity(dto: AuditEventDto) : AuditEntity {
        return AuditEntity(
            uuid = UUID.fromString(dto.uuid),
            message = dto.message,
            service = when (dto.service) {
                in "deal" -> Service.DEAL
                in "conveyor" -> Service.CONVEYOR
                in "application" -> Service.APPLICATION
                else -> Service.DOSSIER
            },
            type =  when (dto.type) {
                in "start" -> Type.START
                in "success" -> Type.SUCCESS
                else -> Type.FAILURE
            }
        )
    }

    fun entityToAuditDto(entity: AuditEntity) : AuditDto {
        val auditDto = AuditDto()
        auditDto.message = entity.message
        auditDto.service = when (entity.service.value) {
            in "deal" -> com.example.credit.application.model.Service.DEAL
            in "conveyor" -> com.example.credit.application.model.Service.CONVEYOR
            in "application" -> com.example.credit.application.model.Service.APPLICATION
            else -> com.example.credit.application.model.Service.DOSSIER
        }
        auditDto.type = when (entity.type.value) {
            in "start" -> com.example.credit.application.model.Type.START
            in "success" -> com.example.credit.application.model.Type.SUCCESS
            else -> com.example.credit.application.model.Type.FAILURE
        }
        auditDto.uuid = entity.uuid
        return auditDto
    }
}