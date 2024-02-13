package com.example.audit.config

import com.example.audit.dto.AuditEventDto
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer

class AuditDeserializer : Deserializer<Any> {
    private val mapper = ObjectMapper()

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        super.configure(configs, isKey)
    }

    override fun deserialize(s: String?, data: ByteArray): Any {
        try {
            mapper.registerModule(JavaTimeModule())
            val jsonNode: JsonNode = mapper.readTree(data)
            return mapper.treeToValue(jsonNode, AuditEventDto::class.java)
        } catch (e: Exception) {
            throw SerializationException("Error when deserializing byte[] to Object", e)
        }
    }

}