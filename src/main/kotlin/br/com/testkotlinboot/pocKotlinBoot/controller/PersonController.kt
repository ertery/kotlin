package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CardDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.PersonDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusUpdate
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/person")
class PersonController(val personService: PersonControllerService) {

    @GetMapping("/{id}")
    fun getPurposeById(@PathVariable id: Long) = personService.findByPersonId(id)

    @PutMapping("/state")
    fun updateState(@RequestBody status: StatusUpdate) = personService.updateState(status)

    @PostMapping("/state")
    fun addState(@RequestBody state: StatusUpdate) = personService.addState(state)

    @PostMapping("")
    fun savePerson(@RequestBody createPerson: PersonDTO): Long {
          return personService.createPerson(createPerson)
    }

    @PostMapping("/{id}/card")
    fun saveCardForPerson(@PathVariable id: Long,
                          @RequestBody card: CardDTO): Long? {
        return personService.addCard(id, card)
    }

}