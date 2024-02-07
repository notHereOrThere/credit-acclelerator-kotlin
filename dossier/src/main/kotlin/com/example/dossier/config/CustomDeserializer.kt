package com.example.dossier.config

import com.example.dossier.dto.Application
import com.example.dossier.dto.EmailDto
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.serialization.Deserializer

class CustomDeserializer : Deserializer<Any> {
    private val mapper = ObjectMapper()

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        super.configure(configs, isKey)
    }

    override fun deserialize(s: String?, data: ByteArray): Any {
        try {
            mapper.registerModule(JavaTimeModule())
            val jsonNode: JsonNode = mapper.readTree(data)
            return try {
                mapper.treeToValue(jsonNode, EmailDto::class.java)
            } catch (e: Exception) {
                try {
                    mapper.treeToValue(jsonNode, Application::class.java)
                } catch (e1: Exception) {
                    throw SerializationException("Error when deserializing byte[] to Object", e1)
                }
            }
        } catch (e: Exception) {
            throw SerializationException("Error when deserializing byte[] to Object", e)
        }
    }

    override fun deserialize(topic: String?, headers: Headers?, data: ByteArray): Any {
        return super.deserialize(topic, headers, data)
    }

    override fun close() {
        super.close()
    }
}
