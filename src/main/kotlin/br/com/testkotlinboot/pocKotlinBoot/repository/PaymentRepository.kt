package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long> {
}