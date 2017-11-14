package br.com.testkotlinboot.pocKotlinBoot.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class PurposeRecord(
        val id: Long,
        val name: String,
        val targetAmmount: Double = 0.0,
        val currentAmmount: Double = 0.0,
        val imageUrl: String = "",
        val description: String = "",
        @get:JsonProperty("isInitial")
        var isInitial: Boolean? = false,
        @JsonIgnore
        val initiatorId: Long = -1,
        val persons: MutableList<PersonList>)