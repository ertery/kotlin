package br.com.testkotlinboot.pocKotlinBoot.entity

import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.FetchType


@Entity
@Table(name = "person")
data class Person(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        var name: String = "",

        @Column(name = "registrationdate")
        var registrationDate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "imagepath")
        var imagePath: String = "",

        @Column(name = "phonenumber")
        var phoneNumber: String = "",

        var email: String = "",

        @ManyToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinTable(name = "purpose_person", joinColumns = arrayOf(
                JoinColumn(name = "person_id", nullable = false, updatable = false)),
                inverseJoinColumns = arrayOf(JoinColumn(name = " purpose_id",
                        nullable = false, updatable = false)))
        var purposes: MutableList<Purpose> = mutableListOf(),

        @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
        var payments: List<Payment> = emptyList()
) {

    @OneToOne()
    @PrimaryKeyJoinColumn
    lateinit var paymentCard: PaymentCard

    @Column(name = "purposestate")
    lateinit var purposeState: PersonPurposeState
}

enum class PersonPurposeState {
    ACCEPT, DECLINE, INVITESEND, INITIAL
}
