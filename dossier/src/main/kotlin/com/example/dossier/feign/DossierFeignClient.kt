package com.example.dossier.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "dossier", url = "\${deal.url}")
interface DossierFeignClient {
    @PostMapping(value = ["/deal/document/{applicationId}/sign"])
    fun signDocuments(@PathVariable applicationId: Long?)
}
