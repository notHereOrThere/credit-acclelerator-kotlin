package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Gender(@field:JsonValue var value: String) {
    MALE("male"), FEMALE("female"), NON_BINARY("non_binary");

}
