package com.example.deal.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin
import java.util.*

@Configuration
class KafkaConfig {
    @Value("\${spring.kafka.producer.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${topic.finish-registration}")
    private val finishRegistration: String? = null

    @Value("\${topic.create-documents}")
    private val createDocument: String? = null

    @Value("\${topic.send-documents}")
    private val sendDocuments: String? = null

    @Value("\${topic.send-ses}")
    private val sendSes: String? = null

    @Value("\${topic.credit-issued}")
    private val creditIssued: String? = null

    @Value("\${topic.application-denied}")
    private val applicationDenied: String? = null

    @Value("\${replicas.count}")
    private val replicasCount: Int? = null

    @Value("\${partitions.count}")
    private val partitionsCount: Int? = null
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        return KafkaAdmin(configs)
    }

    @Bean
    fun adminClient(): AdminClient {
        return AdminClient.create(kafkaAdmin().configurationProperties)
    }

    @Bean
    fun topics(): List<NewTopic> {
        return Arrays.asList(
            NewTopic(finishRegistration, partitionsCount!!, replicasCount!!.toShort()),
            NewTopic(createDocument, partitionsCount, replicasCount.toShort()),
            NewTopic(sendDocuments, partitionsCount, replicasCount.toShort()),
            NewTopic(sendSes, partitionsCount, replicasCount.toShort()),
            NewTopic(creditIssued, partitionsCount, replicasCount.toShort()),
            NewTopic(applicationDenied, partitionsCount, replicasCount.toShort())
        )
    }
}