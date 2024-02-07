package com.example.deal.repository

import com.example.deal.entity.Client
import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository : JpaRepository<Client?, Long?>