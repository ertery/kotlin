package br.com.testkotlinboot.pocKotlinBoot.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "card")
data class PaymentCard(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        var validity: Date? = null,

        @Column(name = "number")
        var cardNumber: String = "",
        @Column(name = "cardholdername")
        var cardholderName: String = ""

) {

    @OneToOne(mappedBy = "paymentCard")
    lateinit var person: Person
}