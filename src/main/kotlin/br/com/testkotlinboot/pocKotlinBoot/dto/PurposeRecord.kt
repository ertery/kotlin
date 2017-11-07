package br.com.testkotlinboot.pocKotlinBoot.dto

import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import java.time.LocalDateTime

class PurposeRecord(val name: String, val creationDate: LocalDateTime, val persons: MutableList<Person>) {

}