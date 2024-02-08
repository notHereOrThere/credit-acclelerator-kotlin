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

    private val countersByStatus = mutableMapOf<ApplicationStatus?, Int>()

    @PostConstruct
    fun init() {

        val statusMap : Map<ApplicationStatus?, Int> =
            applicationRepository.findAll().groupBy { it?.status }
                .mapValues { (_, value) -> value.size  }

        ApplicationStatus.values().forEach { e->
            countersByStatus[e] = 0
        }

        statusMap.forEach { e ->
            countersByStatus[e.key] = e.value
        }

        countersByStatus.forEach { entry ->
            Counter.builder("counter_application_status")
                .description("Number of applications in each status")
                .tag("status ${entry.key}", entry.value.toString())
                .register(meterRegistry)
        }

    }

    fun incrementStatusCounter(status: ApplicationStatus?) {
        countersByStatus[status]?.inc()
    }
}