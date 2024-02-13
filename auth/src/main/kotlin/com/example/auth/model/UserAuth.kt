package com.example.auth.model

import javax.persistence.*

@Entity
@Table(name = "user_auth")
class UserAuth (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var username: String,
    var password: String
)