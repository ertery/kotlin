package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.PaymentCard
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<PaymentCard, Long> {
}