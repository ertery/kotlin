package br.com.testkotlinboot.pocKotlinBoot.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "card")
data class PaymentCard(

        @Id
        @SequenceGenerator(name = "card_seq", sequenceName = "card_id_seq")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
        var id: Long = 0,

        var validity: Date? = null,

        @Column(name = "number")
        var cardNumber: String = "",
        @Column(name = "cardholdername")
        var cardholderName: String = ""

) {
    @JsonIgnore
    @OneToOne(mappedBy = "paymentCard")
    lateinit var person: Person
}