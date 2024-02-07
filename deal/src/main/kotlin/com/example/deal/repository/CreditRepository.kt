package com.example.deal.repository

import com.example.deal.entity.Credit
import org.springframework.data.jpa.repository.JpaRepository

interface CreditRepository : JpaRepository<Credit?, Long?>