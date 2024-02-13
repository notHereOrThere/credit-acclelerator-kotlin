package com.example.audit.repository

import com.example.audit.dto.AuditEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuditRepository : CrudRepository<AuditEntity, UUID> {

    fun findAllByType(type: String): List<AuditEntity>
    fun findAllByService(service: String): List<AuditEntity>
    fun findAllByTypeAndService(type: String, service: String): List<AuditEntity>
}