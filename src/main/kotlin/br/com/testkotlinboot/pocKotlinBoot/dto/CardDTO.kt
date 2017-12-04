package br.com.testkotlinboot.pocKotlinBoot.dto

data class CardDTO(val number: String,
                   val cardholderName: String,
                   val term: String?,
                   val id: Long
) {
}