package com.example.gateway.feign

import com.example.credit.application.model.AuditDto
import com.example.credit.application.model.AuditRequestDto
import com.example.gateway.jwt.JwtRequestModel
import com.example.gateway.jwt.JwtResponseModel
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(name = "gateway-auth", url = "\${auth.url}")
interface AuthFeignClient {

    @PostMapping(value = ["/auth/login"], produces = ["application/json"])
    fun login(@RequestBody request: JwtRequestModel): JwtResponseModel

    @PostMapping(value = ["/auth/register"], produces = ["application/json"])
    fun register(@RequestBody request: JwtRequestModel): JwtResponseModel


}