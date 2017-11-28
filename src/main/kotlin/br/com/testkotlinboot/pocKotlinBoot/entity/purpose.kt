package br.com.testkotlinboot.pocKotlinBoot.entity

import br.com.testkotlinboot.pocKotlinBoot.dto.PaymentList
import br.com.testkotlinboot.pocKotlinBoot.dto.PersonList
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Cascade
import java.time.LocalDateTime
import java.time.ZoneId
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
        var creationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")),

        @Column(name = "finishdate")
        var finishDate: LocalDateTime? = null,

        @Column(name = "targetammount")
        var targetAmmount: Double? = null,

        @Column(name = "currentammount")
        var currentAmmount: Double = 0.0,

        @Column
        var initiatorId: Long = -1,

        @Column(name = "imageurl")
        var imageUrl: String = "",

        var description: String = "",

        @JsonIgnore
        @OneToMany(mappedBy = "purpose", fetch = FetchType.EAGER,
                cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST))
        @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
        var persons: MutableList<PurposePerson> = mutableListOf(),

        @OneToMany(mappedBy = "purpose")
        var payments: MutableList<Payment> = mutableListOf()
) {

    fun toDTO() = PurposeRecord(
            id = this.purposeId,
            name = this.name,
            currentAmmount = this.currentAmmount,
            targetAmmount = this.targetAmmount!!,
            imageUrl = this.imageUrl,
            description = this.description,
            isInitial = false,
            persons = persons.map { pp ->
                PersonList(id = pp.person.personId,
                        name = pp.person.name,
                        imagePath = pp.person.imagePath,
                        payments = pp.person.payments.map { payment ->
                            PaymentList(id = payment.id,
                                    ammount = payment.amount!!,
                                    paymentDate = payment.paymentDate,
                                    paymentMethod = payment.paymentMethod.toString())
                        } as MutableList<PaymentList>)
            } as MutableList<PersonList>
    )
}
