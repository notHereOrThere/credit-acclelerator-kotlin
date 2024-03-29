package com.example.audit.config

import com.redislabs.lettusearch.RediSearchClient
import com.redislabs.lettusearch.StatefulRediSearchConnection
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import io.lettuce.core.RedisURI


@Configuration
@EnableRedisRepositories
class RedisConfig {

    @Value("\${spring.redis.host}")
    private lateinit var redisHost: String

    @Value("\${spring.redis.port}")
    private lateinit var redisPort: String

//    @Bean
//    fun rediSearchClient(lettuceConnectionFactory: LettuceConnectionFactory): StatefulRediSearchConnection<String, String> {
//        val uri = RedisURI.create("${redisHost}:${redisPort}")
//        val client = RediSearchClient.create(uri)
//        return client.connect()
//    }

    @Bean
    fun connectionFactory(): JedisConnectionFactory? {
        val configuration = RedisStandaloneConfiguration()
        configuration.hostName = redisHost
        configuration.port = Integer.valueOf(redisPort)
        return JedisConnectionFactory(configuration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any>? {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(connectionFactory()!!)
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.hashKeySerializer = JdkSerializationRedisSerializer()
        template.valueSerializer = JdkSerializationRedisSerializer()
        template.setEnableTransactionSupport(true)
        template.afterPropertiesSet()
        return template
    }
}
