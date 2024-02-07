package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Position(@field:JsonValue var value: String) {
    WORKER("worker"), MIDDLE_MANAGER("middle_manager"), TOP_MANAGER("top_manager"), OWNER("owner");

}
