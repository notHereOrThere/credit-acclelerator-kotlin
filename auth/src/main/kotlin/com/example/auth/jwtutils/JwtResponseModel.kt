package com.example.auth.jwtutils

import java.io.Serializable


open class JwtResponseModel(val token: String, val login: String) : Serializable {
}