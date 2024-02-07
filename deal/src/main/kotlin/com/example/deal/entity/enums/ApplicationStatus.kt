package com.example.deal.entity.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ApplicationStatus(@field:JsonValue var value: String) {
    PREAPPROVAL("preapproval"), APPROVED("approved"), CC_DENIED("cc_denied"),
    CC_APPROVED("cc_approved"), PREPARE_DOCUMENT("prepare_document"),
    DOCUMENT_CREATED("document_created"), CLIENT_DENIED("client_denied"),
    DOCUMENT_SIGNED("document_signed"), CREDIT_ISSUED("credit_issued");

}