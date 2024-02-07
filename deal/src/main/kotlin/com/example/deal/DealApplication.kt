package com.example.deal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class DealApplication

fun main(args: Array<String>) {
	runApplication<DealApplication>(*args)
}
