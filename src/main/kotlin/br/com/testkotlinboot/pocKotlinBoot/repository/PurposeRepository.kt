package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Purpose
import org.springframework.data.repository.CrudRepository

interface PurposeRepository: CrudRepository<Purpose, Long> {
    fun findByName(name: String): Purpose
}