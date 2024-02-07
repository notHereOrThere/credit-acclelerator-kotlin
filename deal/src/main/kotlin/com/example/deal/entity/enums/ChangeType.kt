package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ChangeType(@field:JsonValue var value: String) {
    AUTOMATIC("automatic"), MANUAL("manual");

}
