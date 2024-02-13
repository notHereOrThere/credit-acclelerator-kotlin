package com.example.gateway.api

import com.example.credit.application.model.AuditDto
import com.example.gateway.feign.AuthFeignClient
import com.example.gateway.jwt.JwtRequestModel
import com.example.gateway.jwt.JwtResponseModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authFeignClient: AuthFeignClient) {

    @Operation(
        operationId = "login",
        summary = "Войти",
    )
    @PostMapping("/login")
    fun login(@RequestBody request: JwtRequestModel) : ResponseEntity<JwtResponseModel> {
        val response = authFeignClient.login(request)
        return ResponseEntity.ok(response)
    }

    @Operation(
        operationId = "register",
        summary = "Регистрация",
    )
    @PostMapping("/register")
    fun register(@RequestBody request: JwtRequestModel) : ResponseEntity<JwtResponseModel> {
        val response = authFeignClient.register(request)
        return ResponseEntity.ok(response)
    }
}