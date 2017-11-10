package br.com.testkotlinboot.pocKotlinBoot.dto


data class CreatePurpose(var name: String,
                         var targetAmount: Double = 0.0,
                         var imageUrl: String = "",
                         var description: String = "",
                         var initiatorId: Long,
                         var persons: MutableList<UnregisteredPerson>)