package br.com.testkotlinboot.pocKotlinBoot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters



@EntityScan(basePackageClasses = arrayOf(PocKotlinBootApplication::class, Jsr310JpaConverters::class))
@SpringBootApplication
open class PocKotlinBootApplication

fun main(args: Array<String>) {
    SpringApplication.run(PocKotlinBootApplication::class.java, *args)
}
