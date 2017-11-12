package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/person")
class PersonController(val service: PersonControllerService) {

    @GetMapping("/{id}")
    fun getPurposeById(@PathVariable id: Long )
            = service.findByPersonId(id)
}