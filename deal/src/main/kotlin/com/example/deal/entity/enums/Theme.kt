package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Theme(@field:JsonValue var value: String) {
    CRITICAL("critical"), INFO("info"), NOTIFICATION("notification");

}
