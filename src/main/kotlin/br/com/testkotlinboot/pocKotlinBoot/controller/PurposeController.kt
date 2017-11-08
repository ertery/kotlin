package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.service.PurposeControllerService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/purpose")
class PurposeController(val service: PurposeControllerService) {

    @RequestMapping("/")
    fun getPurposes() = service.getPurposes()

    @GetMapping("/id")
    fun getPurposeById(@RequestParam(value = "id") id: Long) = service.getPurposeById(id)

    @GetMapping("/name")
    fun getPurposeByName(@RequestParam(value = "name") name: String) = service.getPurposeByName(name)
}