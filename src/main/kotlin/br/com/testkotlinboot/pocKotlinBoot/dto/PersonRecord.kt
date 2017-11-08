package br.com.testkotlinboot.pocKotlinBoot.dto

import br.com.testkotlinboot.pocKotlinBoot.entity.PaymentCard
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import java.time.LocalDateTime

class PersonRecord(val paymentCard: PaymentCard, val purposeState: PersonPurposeState, val name: String,
                   val registrationDate: LocalDateTime)