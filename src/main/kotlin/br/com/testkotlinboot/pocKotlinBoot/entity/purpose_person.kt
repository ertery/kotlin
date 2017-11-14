package br.com.testkotlinboot.pocKotlinBoot.entity

import br.com.testkotlinboot.pocKotlinBoot.dto.PersonRecord
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import java.io.Serializable
import javax.persistence.*

@Entity(name = "purpose_person")
data class PurposePerson(
        @Id
        @ManyToOne
        @JoinColumn(name = "purpose_id")
        val purpose: Purpose = Purpose(),

        @Id
        @ManyToOne
        @JoinColumn(name = "person_id")
        val person: Person = Person()
) : Serializable {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "purpose_state")
    lateinit var purposeState: PersonPurposeState

}