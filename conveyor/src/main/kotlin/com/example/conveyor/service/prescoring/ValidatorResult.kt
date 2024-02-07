package com.example.conveyor.service.prescoring

data class ValidatorResult(
    private var codeErr: Int = 0,
    private var textErr: String = "",
    private var isValid: Boolean = true
) {
    fun getIsValid(): Boolean {
        return isValid
    }
}
