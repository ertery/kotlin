package br.com.testkotlinboot.pocKotlinBoot.entity

import javax.persistence.*

@Entity
@Table(name = "device_info")
data class Device(

        @Id
        @SequenceGenerator(name = "device_seq", sequenceName = "device_info_id_seq")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
        val id: Long? = -1,

        @Column
        val token: String = ""
) {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    lateinit var person: Person
}