package com.example.audit.service

import com.example.audit.dto.AuditEntity
import com.example.audit.dto.AuditEventDto
import com.example.audit.mapper.AuditMapper
import com.example.audit.repository.AuditRepository
import com.example.credit.application.model.AuditDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*


@Service
class AuditServiceImpl (private val auditRepository: AuditRepository,
                        private val auditMapper: AuditMapper,
                        private val redisTemplate : RedisTemplate<String, Any>) : AuditService {

    val gson = Gson()

    override fun persistToRedis(dto: AuditEventDto) {

        val auditEntity = auditMapper.eventDtoToEntity(dto)

        val key = "audit:entities:${auditEntity.uuid}"
        val serviceIndexKey = "audit:index:service:${auditEntity.service.value}"
        val typeIndexKey = "audit:index:type:${auditEntity.type.value}"
        val combinedIndexKey = "audit:index:type:${auditEntity.type.value}:service:${auditEntity.service.value}"

        // Сериализация и сохранение объекта
        redisTemplate.opsForHash<String, String>().put("audit:entities", auditEntity.uuid.toString(), gson.toJson(auditEntity))

        // Добавление UUID в индексы
        redisTemplate.opsForSet().add(serviceIndexKey, auditEntity.uuid.toString())
        redisTemplate.opsForSet().add(typeIndexKey, auditEntity.uuid.toString())
        redisTemplate.opsForSet().add(combinedIndexKey, auditEntity.uuid.toString())

//        auditRepository.save(auditMapper.eventDtoToEntity(dto))
    }



    override fun getAuditEventByUUID(uuid: String) : AuditDto {
        return auditMapper.entityToAuditDto(
            auditRepository.findById(UUID.fromString(uuid)).get())
    }



    override fun findAllByTypeAndService(type: String?, service: String?): List<AuditDto> {

        if (type == null && service == null) {
            return auditRepository.findAll().map { auditMapper.entityToAuditDto(it) }
        }

        val combinedIndexKey = java.lang.StringBuilder("audit:index")

        if (type != null) {
            combinedIndexKey.append(":type:")
                .append("$type")
        }
        if (service != null) {
            combinedIndexKey.append(":service:")
                .append("$service")
        }


        val uuids = redisTemplate.opsForSet().members(combinedIndexKey.toString())

        val objectMapper = ObjectMapper()

        if (uuids != null) {
            return uuids.mapNotNull { uuid ->
                val entityString = redisTemplate.opsForHash<String, String>().get("audit:entities", uuid)
                auditMapper.entityToAuditDto(objectMapper.readValue(entityString, AuditEntity::class.java))
            }
        } else throw RuntimeException("No records")
    }
}