package com.example.audit.service

import com.example.audit.dto.AuditEventDto
import com.example.audit.mapper.AuditMapper
import com.example.audit.repository.AuditRepository
import com.example.credit.application.model.AuditDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*


@Service
class AuditServiceImpl (private val auditRepository: AuditRepository,
                        private val auditMapper: AuditMapper,
                        private val redisTemplate : RedisTemplate<String, Any>) : AuditService {


    override fun persistToRedis(dto: AuditEventDto) {
        auditRepository.save(auditMapper.eventDtoToEntity(dto))
    }



    override fun getAuditEventByUUID(uuid: String) : AuditDto {
        return auditMapper.entityToAuditDto(
            auditRepository.findById(UUID.fromString(uuid)).get())
    }



    override fun findAllByTypeAndService(type: String?, service: String?): List<AuditDto> {

        if (type != null && service != null) {
            return auditRepository.findAllByTypeAndService(type, service).map { auditMapper.entityToAuditDto(it) }
        }
        if (type != null) {
            return auditRepository.findAllByType(type).map { auditMapper.entityToAuditDto(it) }
        }
        if (service != null) {
            return auditRepository.findAllByService(service).map { auditMapper.entityToAuditDto(it) }
        }
        return auditRepository.findAll().map { auditMapper.entityToAuditDto(it) }
    }
}