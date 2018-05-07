package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CardDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.PersonDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusUpdate
import br.com.testkotlinboot.pocKotlinBoot.dto.TokenDTO
import br.com.testkotlinboot.pocKotlinBoot.service.AuthService
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/person")
class PersonController(val personService: PersonControllerService, val authService: AuthService) {

    @GetMapping("/")
    fun getPurposeByPersonId(@RequestHeader("аuthorization", required = false) authorization: String?): ResponseEntity<String> {
        if (authorization == null && authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        personService.findByPersonId(authService.decodeToken(authorization!!)!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/facebook/{id}")
    fun getPersonByFacebookId(@PathVariable id: String) = personService.findPersonByFacebookId(id)

    @PutMapping("/state")
    fun updateState(@RequestHeader("аuthorization", required = false) authorization: String?,
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

}