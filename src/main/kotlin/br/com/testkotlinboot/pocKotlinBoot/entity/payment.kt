package br.com.testkotlinboot.pocKotlinBoot.entity

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
    @Enumerated(EnumType.STRING)
    lateinit var state: PaymentState

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentmethod")
    lateinit var paymentMethod: PaymentMethod

    @Enumerated(EnumType.STRING)
    lateinit var channel: Channel

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    lateinit var person: Person

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purpose_id")
    lateinit var purpose: Purpose
}

enum class PaymentState {
    NEW, INPROGRESS, DONE, DECLINED
}

enum class PaymentMethod {
    CASH, CLEARING, DEBT
}

enum class Channel {
    IOS, Telegramm
}