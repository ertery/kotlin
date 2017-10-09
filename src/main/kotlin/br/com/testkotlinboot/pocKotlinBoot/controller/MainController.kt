package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.web.bind.annotation.*


@RestController
class MainController(val repository: PurposeRepository) {

    @RequestMapping("/")
    fun getPurposes() = repository.findAll()

}