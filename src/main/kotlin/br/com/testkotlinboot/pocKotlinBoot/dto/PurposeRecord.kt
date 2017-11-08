package br.com.testkotlinboot.pocKotlinBoot.dto

import java.time.LocalDateTime

class PurposeRecord(val name: String,
                    val creationDate: LocalDateTime,
                    val targetAmount: Double = 0.0,
                    val imageUrl: String = "",
                    val description: String = "",
                    val persons: MutableList<String>)