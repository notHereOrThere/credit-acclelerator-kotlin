package com.example.auth.controller

import com.example.auth.jwtutils.JwtRequestModel
import com.example.auth.jwtutils.JwtResponseModel
import com.example.auth.jwtutils.JwtUserDetailsService
import com.example.auth.jwtutils.TokenManager
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
class AuthController (private val jwtUserDetailsService: JwtUserDetailsService,
                      private val authenticationManager: AuthenticationManager,
                      private val tokenManager: TokenManager,
) {
    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }

    @PostMapping("/login")
    @Throws(Exception::class)
    fun login(@RequestBody request: JwtRequestModel) : ResponseEntity<JwtResponseModel> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (e : DisabledException) {
            throw Exception("USER_DISABLED", e);
        } catch (e : BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", e)
        }
        val userDetails = request.username?.let { jwtUserDetailsService.loadUserByUsername(it) }

        val jwtToken = userDetails?.let { tokenManager.generateJwtToken(it) }

        return ResponseEntity.ok(jwtToken?.let { JwtResponseModel(it, request.username) })
    }

    @PostMapping("/register")
    @Throws(Exception::class)
    fun register(@RequestBody request: JwtRequestModel) : ResponseEntity<JwtResponseModel> {

        val userDetails = jwtUserDetailsService.register(request)

        val token = tokenManager.generateJwtToken(userDetails)

        return ResponseEntity.ok( JwtResponseModel(token, request.username))
    }
}