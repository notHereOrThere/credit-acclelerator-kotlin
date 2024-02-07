package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class MaritalStatus(@field:JsonValue var value: String) {
    MARRIED("married"), SINGLE("single"), DIVORCE("divorce"), WIDOW_WIDOWER("widow_widower");

}
