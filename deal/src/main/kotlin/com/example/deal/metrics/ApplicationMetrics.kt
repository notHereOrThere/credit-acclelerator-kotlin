package com.example.deal.metrics

import com.example.deal.entity.enums.ApplicationStatus
import com.example.deal.repository.ApplicationRepository
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class ApplicationMetrics(
    private val meterRegistry: MeterRegistry,
    private val applicationRepository: ApplicationRepository
    ) {

    private val countersByStatus = ApplicationStatus.values().associateWith { status ->
        meterRegistry.counter("application_status", "status", status.name)
    }

    @PostConstruct
    fun init() {
        val statusCounts = applicationRepository.findAll().groupingBy { it?.status }.eachCount()
        statusCounts.forEach { (status, count) ->
            val counter = countersByStatus[status]
            repeat(count) { counter?.increment() }
        }
    }

    fun incrementStatusCounter(status: ApplicationStatus?) {
        countersByStatus[status]?.increment()
    }
}