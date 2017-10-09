package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.PaymentCard
import org.springframework.data.repository.CrudRepository

interface CardRepository : CrudRepository<PaymentCard, Long> {
}