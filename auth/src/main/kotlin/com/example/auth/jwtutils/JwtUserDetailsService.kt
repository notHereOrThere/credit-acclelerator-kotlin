package com.example.auth.jwtutils

import com.example.auth.model.AuthUserRepository
import com.example.auth.model.UserAuth
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class JwtUserDetailsService (private val authUserRepository: AuthUserRepository,
                             private val passwordEncoder: PasswordEncoder) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val authUser = authUserRepository.findByUsername(username)
        return User(
            authUser.username,
            authUser.password,
            ArrayList()
        )
    }

    fun register(jwtRequestModel: JwtRequestModel): UserDetails {
        val authUser = UserAuth(
            username = jwtRequestModel.username,
            password = passwordEncoder.encode(jwtRequestModel.password)
        )
        authUserRepository.save(authUser)
        return User(
            authUser.username,
            authUser.password,
            ArrayList()
        )
    }
}