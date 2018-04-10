package br.com.testkotlinboot.pocKotlinBoot.entity

import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*


@Entity
@Table(name = "unregistered_person")
data class Unregistered(
        @Id
        @SequenceGenerator(name = "person_seq", sequenceName = "person_id_seq")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
        var id: Long = 0,

        @Column
        val phone: String,

        @Column
        val code: String,

        @Column(name = "created")
        val sendTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"))) {}