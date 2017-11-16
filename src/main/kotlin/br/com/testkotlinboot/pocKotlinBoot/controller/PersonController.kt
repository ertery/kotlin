package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePerson
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusUpdate
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/person")
class PersonController(val personService: PersonControllerService) {

    @GetMapping("/{id}")
    fun getPurposeById(@PathVariable id: Long) = personService.findByPersonId(id)

    @PutMapping("/status")
    fun updateStatus(@RequestBody status: StatusUpdate) = personService.updateStatus(status)

    @PostMapping("")
    fun savePerson(@RequestBody createPerson: CreatePerson): Long {
          return personService.createPerson(createPerson)
    }

}