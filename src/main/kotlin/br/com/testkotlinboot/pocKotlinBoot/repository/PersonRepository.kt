package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import org.springframework.data.repository.CrudRepository

interface PersonRepository: CrudRepository<Person, Long> {
}