package com.example.dossier.dto.inner

import com.example.deal.entity.enums.ChangeType
import com.example.dossier.dto.Application
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.*

class StatusHistory : Serializable {
    private var statusHistoryId: Long? = null

    @JsonIgnore
    private var application: Application? = null
    private var status: String? = null
    private var time: Date? = null
    private var changeType: ChangeType? = null
}
