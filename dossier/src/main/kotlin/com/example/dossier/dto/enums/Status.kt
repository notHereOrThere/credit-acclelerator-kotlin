package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Status(@field:JsonValue var value: String) {
    CALCULATED("calculated"), ISSUED("issued");

}
