package br.com.testkotlinboot.pocKotlinBoot.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class PurposeModifyDTO(val imageUrl: String?,
                            val description: String?,
                            val targetAmmount: Double?,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM") val finishDate: LocalDate?,
                            val name: String?)
