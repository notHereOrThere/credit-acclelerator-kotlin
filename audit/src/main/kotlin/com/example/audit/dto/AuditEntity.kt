package com.example.audit.dto

import com.example.deal.entity.enums.Service
import com.example.deal.entity.enums.Type
//import com.redis.om.spring.annotations.Searchable
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.util.UUID

@RedisHash("Audit")
data class AuditEntity (
    @Id
    var uuid: UUID,
    val type: Type,
    var service: Service,
    var message: String
)
