package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Purpose
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PurposeRepository : JpaRepository<Purpose, Long> {

    @Query("Select p from Purpose p where p.name like %:name%")
    fun findByName(@Param(value = "name") name: String): Purpose
}