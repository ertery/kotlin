package br.com.testkotlinboot.pocKotlinBoot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import org.springframework.scheduling.annotation.EnableScheduling


@EntityScan(basePackageClasses = arrayOf(PocKotlinBootApplication::class, Jsr310JpaConverters::class))
@SpringBootApplication
@EnableScheduling
open class PocKotlinBootApplication

fun main(args: Array<String>) {
    SpringApplication.run(PocKotlinBootApplication::class.java, *args)
}
