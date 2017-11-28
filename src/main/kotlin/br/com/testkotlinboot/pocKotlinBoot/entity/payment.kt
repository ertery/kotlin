package br.com.testkotlinboot.pocKotlinBoot.entity

import br.com.testkotlinboot.pocKotlinBoot.dto.PaymentResponseDTO
import br.com.testkotlinboot.pocKotlinBoot.enums.Channel
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentMethod
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentState
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.persistence.*


@Entity
@Table(name = "payment")
data class Payment(

        @Id
        @SequenceGenerator(name = "payment_seq", sequenceName = "payment_id_seq")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
        var id: Long = 0,
        var amount: Double = 0.0,

        @Column(name = "paymentdate")
        var paymentDate: LocalDate = LocalDate.now(ZoneId.of("Europe/Moscow"))

) {
    @Enumerated(value = EnumType.STRING)
    lateinit var state: PaymentState

    @Enumerated(value = EnumType.STRING)
    @Column(name = "paymentmethod")
    lateinit var paymentMethod: PaymentMethod

    @Enumerated(value = EnumType.STRING)
    lateinit var channel: Channel

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    lateinit var person: Person

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purpose_id")
    lateinit var purpose: Purpose

    fun toPaymentResponseDTO() = PaymentResponseDTO (
            amount = this.amount,
            paymentDate = this.paymentDate,
            cardNumber = CardUtilClass.maskNumber(this.person.paymentCard.cardNumber),
            purposeDescription = this.purpose.description,
            personName = this.person.name
    )
}
