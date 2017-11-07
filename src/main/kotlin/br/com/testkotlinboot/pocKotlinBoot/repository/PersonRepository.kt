package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository: JpaRepository<Person, Long> {
}