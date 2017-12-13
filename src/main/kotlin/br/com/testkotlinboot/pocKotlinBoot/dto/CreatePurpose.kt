package br.com.testkotlinboot.pocKotlinBoot.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate


data class CreatePurpose(var name: String,
                         var targetAmmount: Double = 0.0,
                         var imageUrl: String = "",
                         var description: String = "",
                         var initiatorId: Long,
                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM")
                         var finishDate: LocalDate = LocalDate.of(2020, 8 , 29),
                         var persons: MutableList<UnregisteredPerson>)