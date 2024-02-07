package com.example.dossier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class DossierApplication

fun main(args: Array<String>) {
    runApplication<DossierApplication>(*args)
}
