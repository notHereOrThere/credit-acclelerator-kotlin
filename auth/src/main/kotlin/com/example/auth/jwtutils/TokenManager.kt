package com.example.auth.jwtutils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


@Component
class TokenManager : Serializable {
    @Value("\${secret}")
    private val jwtSecret: String? = null

    @Value("\${expiration}")
    private val tokenExpiration : String? = null

    fun generateJwtToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder().setClaims(claims).setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + Integer.valueOf(tokenExpiration) * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret).compact()
    }

    fun validateJwtToken(token: String?, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        val isTokenExpired = claims.expiration.before(Date())
        return username == userDetails.username && !isTokenExpired
    }

    fun getUsernameFromToken(token: String?): String {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        return claims.subject
    }

}