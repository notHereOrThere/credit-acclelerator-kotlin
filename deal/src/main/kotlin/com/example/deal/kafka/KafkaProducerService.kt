package com.example.deal.kafka

import lombok.RequiredArgsConstructor
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class KafkaProducerService (private val kafkaTemplate: KafkaTemplate<String, Any>) {

    fun send(topic: String?, message: Any) {
        kafkaTemplate.send(topic!!, message)
    }
}
