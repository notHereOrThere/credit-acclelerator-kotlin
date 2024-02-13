package com.example.auth.model;

import org.springframework.data.jpa.repository.JpaRepository

interface AuthUserRepository : JpaRepository<UserAuth, Long> {
    fun findByUsername(username : String) : UserAuth
}