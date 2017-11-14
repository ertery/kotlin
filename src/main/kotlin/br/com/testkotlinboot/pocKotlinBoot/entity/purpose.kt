package br.com.testkotlinboot.pocKotlinBoot.entity

import br.com.testkotlinboot.pocKotlinBoot.dto.PersonList
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Cascade
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "purpose")
data class Purpose(

        @Id
        @SequenceGenerator(name = "purpose_seq", sequenceName = "purpose_id_seq")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purpose_seq")
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

        @Column
        var initiatorId: Long = -1,

        @Column(name = "imageurl")
        var imageUrl: String = "",

        var description: String = "",

        @JsonIgnore
        @ManyToMany(mappedBy = "purposes", fetch = FetchType.EAGER,
                cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST))
        @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
        var persons: MutableList<Person> = mutableListOf(),

        @OneToMany(mappedBy = "purpose")
        var payments: List<Payment> = emptyList()
) {

    fun toDTO(): PurposeRecord = PurposeRecord(
            id = this.purposeId,
            name = this.name,
            currentAmmount = this.currentAmmount,
            targetAmmount = this.targetAmmount!!,
            imageUrl = this.imageUrl,
            description = this.description,
            isInitial = false,
            persons = this.persons.map { (personId, name1, registrationDate, imagePath) -> PersonList(id = personId, name = name1, imagePath = imagePath)} as MutableList<PersonList>
    )
}
