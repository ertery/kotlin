package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/person")
class PersonController(val service: PersonControllerService) {

    @GetMapping("/id")
    fun getPurposeById(@RequestParam(value = "id") id: Long, @RequestParam(value = "init", required=false) init: Boolean = false )
            = service.findByPersonId(id, init)
}