package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Unregistered
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface RegistrationRepository: JpaRepository<Unregistered, Long>{

    fun findBySendTimeBefore(sendTime: LocalDateTime): List<Unregistered>
    fun findByCode(code: String):Unregistered?
    fun findByPhone(phone: String): List<Unregistered>
}