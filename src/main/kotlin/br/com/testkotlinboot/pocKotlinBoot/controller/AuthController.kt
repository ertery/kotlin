package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.SMSCodeDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.UnregisteredPerson
import br.com.testkotlinboot.pocKotlinBoot.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authorize")
class AuthController(val authService: AuthService) {

    @PostMapping("")
    fun sendSMSCode(@RequestBody person: UnregisteredPerson): ResponseEntity<String> {
        return if (authService.sendSms(person))
            ResponseEntity("месседж послан", HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/code")
    fun checkSMSCode(@RequestBody auth: SMSCodeDTO): ResponseEntity<String> {
        val token = authService.checkSMSCode(auth)
        return if (token == null) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
        else ResponseEntity.ok().body(token)
    }
}