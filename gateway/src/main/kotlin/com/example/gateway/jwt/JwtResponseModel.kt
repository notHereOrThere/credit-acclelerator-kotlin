package com.example.gateway.jwt

import java.io.Serializable


open class JwtResponseModel(val token: String, val login: String) : Serializable {
}