package com.example.gateway.jwt

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class TokenManager : Serializable {
    @Value("\${secret}")
    private val jwtSecret: String? = null

    fun validateJwtToken(token: String?): Boolean {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        val isTokenExpired = claims.expiration.before(Date())
        return !isTokenExpired
    }

    fun getUsernameFromToken(token: String?): String {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        return claims.subject
    }

}