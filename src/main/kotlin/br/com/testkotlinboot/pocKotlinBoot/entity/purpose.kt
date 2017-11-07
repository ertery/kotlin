package br.com.testkotlinboot.pocKotlinBoot.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "purpose")
data class Purpose(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var purposeId: Long = 0,

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

        var description: String = ""
) {
    @JsonIgnore
    @ManyToMany(mappedBy = "purposes", fetch = FetchType.EAGER)
    var persons: MutableList<Person> = mutableListOf()

    @OneToMany(mappedBy = "purpose")
    var payments: List<Payment> = emptyList()
}