package br.com.testkotlinboot.pocKotlinBoot.dto

import java.time.LocalDateTime


data class CreatePurpose(var name: String,
                         var targetAmmount: Double = 0.0,
                         var imageUrl: String = "",
                         var description: String = "",
                         var initiatorId: Long,
                         var finishDate: LocalDateTime = LocalDateTime.of(2020, 8 , 29, 19, 30, 40),
                         var persons: MutableList<UnregisteredPerson>)