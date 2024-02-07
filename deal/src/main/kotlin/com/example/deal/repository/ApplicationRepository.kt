package com.example.deal.repository

import com.example.deal.entity.Application
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository : JpaRepository<Application?, Long?>