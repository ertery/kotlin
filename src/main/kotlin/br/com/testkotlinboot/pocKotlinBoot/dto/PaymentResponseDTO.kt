package br.com.testkotlinboot.pocKotlinBoot.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class PaymentResponseDTO(val amount: Double,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/YYYY")
                              val paymentDate: LocalDate,
                              val cardNumber: String,
                              val purposeDescription: String,
                              val personName: String,
                              val personId: Long)