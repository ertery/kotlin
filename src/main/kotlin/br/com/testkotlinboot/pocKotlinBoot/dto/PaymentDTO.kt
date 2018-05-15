package br.com.testkotlinboot.pocKotlinBoot.dto

data class PaymentDTO(val ammount: Double,
                      val paymentMethod: String,
                      val channel: String,
                      val state: String) {}