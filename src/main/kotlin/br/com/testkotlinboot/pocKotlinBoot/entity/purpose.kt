package br.com.testkotlinboot.pocKotlinBoot.entity

import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.FetchType



@Entity
@Table(name = "purpose")
data class Purpose(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var purpose_id: Long = 0,

        var name: String = "",

        @Column(name = "creationdate")
        var creationDate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "finishdate")
        var finishDate: LocalDateTime? = null,

        @Column(name = "targetammount")
        var targetAmmount: Double? = null,

        @Column(name = "currentammount")
        var currentAmmount: Double = Double.MIN_VALUE,

        @Column(name = "imageurl")
        var imageUrl: String = "",

        var description: String = "",

        @ManyToMany(mappedBy = "purposes")
        var persons: List<Person> = emptyList(),

        @OneToMany(mappedBy = "purpose", fetch = FetchType.EAGER)
        var payments: List<Payment> = emptyList()
)