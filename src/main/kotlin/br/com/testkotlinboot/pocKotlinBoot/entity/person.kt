package br.com.testkotlinboot.pocKotlinBoot.entity

import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.FetchType


@Entity
@Table(name = "person")
data class Person(

        @Id
        @SequenceGenerator(name = "person_seq", sequenceName = "person_id_seq")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
        var personId: Long = 0,

        var name: String = "",

        @Column(name = "registrationdate")
        var registrationDate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "imagepath")
        var imagePath: String = "",

        @Column(name = "phonenumber")
        var phoneNumber: String = "",

        var email: String = "",

        @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
        var payments: List<Payment> = emptyList(),

        @OneToMany(mappedBy = "person", cascade = arrayOf(CascadeType.ALL))
        var purposes: MutableList<PurposePerson> = mutableListOf()
) {

    @OneToOne(cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE))
    @PrimaryKeyJoinColumn
    var paymentCard: PaymentCard = PaymentCard()
}

