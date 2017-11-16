package br.com.testkotlinboot.pocKotlinBoot.dto

data class SavePurposeResponse(val purposeId: Long, val personsId: MutableList<SavedPerson> ) {
}