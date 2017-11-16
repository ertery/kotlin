package br.com.testkotlinboot.pocKotlinBoot.dto

data class PersonList(val id: Long,
                      val imagePath: String,
                      val name: String,
                      val payments: MutableList<PaymentList> = mutableListOf())