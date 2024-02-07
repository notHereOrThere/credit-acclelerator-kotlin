package com.example.conveyor.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class EmploymentStatus(@field:JsonValue var value: String) {
    UNEMPLOYED("unemployed"), SELF_EMPLOYED("self_unemployed"), EMPLOYED("employed"), BUSINESS_OWNER("business_owner");

}
