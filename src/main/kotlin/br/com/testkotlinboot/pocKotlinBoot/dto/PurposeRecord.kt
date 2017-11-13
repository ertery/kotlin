package br.com.testkotlinboot.pocKotlinBoot.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

class PurposeRecord(
        val id: Long, val name: String,
        val currentAmmount: Double = 0.0,
        val targetAmmount: Double = 0.0,
        val imageUrl: String = "",
        val description: String = "",
        var isInitial: Boolean? = false,
        @JsonIgnore
        val initiatorId: Long = -1,
        val persons: MutableList<PersonList>)