package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Type(@field:JsonValue var value: String) {
    START("start"), SUCCESS("success"), FAILURE("failure");

}
