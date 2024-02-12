package com.example.deal.api

import com.example.deal.entity.Application
import com.example.deal.service.DealService
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = "Админское Api для работы МС deal")
@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
class DealAdminController (private val dealService: DealService) {


    @Operation(
        summary = "Получить все заявки.",
        responses = [ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ), ApiResponse(
            responseCode = "500",
            description = "Error processing the request"
        )]
    )
    @GetMapping("/application")
    fun allApplications(): ResponseEntity<List<Application>> {
        val applications: List<Application> = dealService.fetchAllApplications() as List<Application>
        return ResponseEntity.ok(applications)
    }

    @GetMapping("/application/{applicationId}")
    @Operation(
        summary = "Получить заявку по id.",
        responses = [ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ), ApiResponse(responseCode = "500", description = "Error processing the request")]
    )
    fun getApplicationById(@PathVariable applicationId: Long?): ResponseEntity<Application> {
        val application: Application? = dealService.getApplicationById(applicationId)
        return ResponseEntity.ok(application)
    }
}