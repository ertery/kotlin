package br.com.testkotlinboot.pocKotlinBoot.entity

import java.time.LocalDateTime
import java.time.ZoneId
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
        var registrationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")),

        @Column(name = "imagepath")
        var imagePath: String = "",

        @Column(name = "phonenumber")
        var phoneNumber: String = "",

        var email: String = "",

        @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
        var payments: MutableList<Payment> = mutableListOf(),

        @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
        var devices: MutableList<Device> = mutableListOf(),

        @OneToMany(mappedBy = "person", cascade = arrayOf(CascadeType.ALL))
        var purposes: MutableList<PurposePerson> = mutableListOf(),

        @Column(name = "facebookid")
        var facebookId: String = "",

        @Column
        var token: String = "",

        @Column(name = "yandex")
        var yandexConnected: Boolean = false
) {
    @OneToOne(mappedBy = "person", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    var paymentCard: PaymentCard? = null

    override fun toString(): String =
            "Person(personId=$personId, name='$name', registrationDate=$registrationDate, imagePath='$imagePath', phoneNumber='$phoneNumber', purposes=$purposes, facebookId='$facebookId')"


}

