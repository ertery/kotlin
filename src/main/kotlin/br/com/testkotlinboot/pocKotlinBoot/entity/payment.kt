package br.com.testkotlinboot.pocKotlinBoot.entity

import br.com.testkotlinboot.pocKotlinBoot.enums.Channel
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentMethod
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentState
import java.time.LocalDate
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "payment")
data class Payment(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var ammount: Double? = null,

        @Column(name = "paymentdate")
        var paymentDate: LocalDate = LocalDate.now()

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
}
