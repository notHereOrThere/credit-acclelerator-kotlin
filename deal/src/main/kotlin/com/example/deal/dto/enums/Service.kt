package com.example.deal.dto.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Service(@field:JsonValue var value: String) {
    APPLICATION("application"),
    DEAL("deal"),
    CONVEYOR("conveyor"),
    DOSSIER("dossier");

}
