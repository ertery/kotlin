package br.com.testkotlinboot.pocKotlinBoot.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class PaymentList(val id: Long,
                       val ammount: Double = 0.0,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM")
                       val paymentDate: LocalDate,
                       val paymentMethod: String)