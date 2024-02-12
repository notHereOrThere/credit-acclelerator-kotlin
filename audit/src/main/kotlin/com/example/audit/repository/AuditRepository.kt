package com.example.audit.repository

import com.example.audit.dto.AuditEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuditRepository : PagingAndSortingRepository<AuditEntity, UUID> {

//    fun findAllByType(type: String, pageable: Pageable): Page<AuditEntity>
//    fun findAllByService(service: String, pageable: Pageable): Page<AuditEntity>
//    fun findAllByTypeAndService(type: String, service: String, pageable: Pageable): Page<AuditEntity>
}