package com.example.gateway.config

import com.example.gateway.jwt.TokenManager
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtCheckerInterceptor (private val tokenManager: TokenManager): HandlerInterceptor {

    @Throws(ServletException::class, IOException::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tokenHeader = request.getHeader("Authorization")
        var username: String? = null
        var token: String? = null
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7)
            try {
                username = tokenManager.getUsernameFromToken(token)
            } catch (e: IllegalArgumentException) {
                println("Unable to get JWT Token")
            } catch (e: ExpiredJwtException) {
                println("JWT Token has expired")
            }
        } else {
            println("not authorized")
        }
        return tokenManager.validateJwtToken(token)
    }
}