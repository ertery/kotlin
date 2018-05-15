package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.*
import br.com.testkotlinboot.pocKotlinBoot.service.AuthService
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/person")
class PersonController(val personService: PersonControllerService, val authService: AuthService) {

    @GetMapping("/")
    fun getPurposeByPersonId(@RequestHeader("Authorization", required = false) authorization: String?): ResponseEntity<Any> {
        if (authorization == null && authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(personService.findByPersonId(authService.decodeToken(authorization!!)!!), HttpStatus.OK)
    }

    @GetMapping("/facebook/{id}")
    fun getPersonByFacebookId(@PathVariable id: String) = personService.findPersonByFacebookId(id)

    @PutMapping("/state")
    fun updateState(@RequestHeader("Authorization", required = false) authorization: String?,
                    @RequestBody status: StatusUpdate) : ResponseEntity<String> {
        if (authorization == null && authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        personService.updateState(status, authorization!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/state")
    fun addState(@RequestBody state: StatusUpdate) = personService.addState(state)

    @PostMapping("")
    fun savePerson(@RequestBody createPerson: PersonDTO): Long = personService.createOrUpdatePerson(createPerson)

    @PutMapping("/token")
    fun addTokenForPerson(@RequestBody token: TokenDTO,
                          @RequestHeader("Authorization", required = false) authorization: String?): ResponseEntity<String> {
        if (authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        personService.addToken(token, authorization.toString())
        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping("")
    fun updatePerson(@RequestBody person: PersonModifyDTO,
            @RequestHeader("Authorization", required = false) authorization: String?):ResponseEntity<Any>{
        if (authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        personService.updatePerson(person, authorization)
        return ResponseEntity(HttpStatus.OK)
    }

}