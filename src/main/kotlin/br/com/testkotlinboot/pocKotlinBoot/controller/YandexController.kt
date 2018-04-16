package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.YandexToggleDTO
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/yandex")
class YandexController(val personService: PersonControllerService) {

    @PutMapping("/")
    fun toggleStatus(@RequestHeader(value = "token", required = true) token: String?,
                     @RequestBody toggle: YandexToggleDTO): ResponseEntity<String> {
        return if (personService.togglePerson(toggle, token)) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}