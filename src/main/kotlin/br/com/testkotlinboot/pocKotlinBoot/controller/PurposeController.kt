package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePurpose
import br.com.testkotlinboot.pocKotlinBoot.dto.UnregisteredPerson
import br.com.testkotlinboot.pocKotlinBoot.entity.Purpose
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import br.com.testkotlinboot.pocKotlinBoot.service.PurposeControllerService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/purpose")
class PurposeController(val purposeService: PurposeControllerService, val personService: PersonControllerService) {

    @RequestMapping("/")
    fun getPurposes() = purposeService.getPurposes()

    @GetMapping("/{id}")
    fun getPurposeById(@PathVariable id: Long) = purposeService.getPurposeById(id)

    @GetMapping("/find")
    fun getPurposeByName(@RequestParam(value = "byName", required = false) name: String?,
                         @RequestParam(value = "byId", required = false) id: Long?): Any {

        return when {
            name != null -> purposeService.findPurposeByName(name)
            id != null -> personService.findByPersonId(id)
            else -> mutableListOf<String>()
        }
    }

    @PostMapping("/add")
    fun addPurpose(@RequestBody newPurpose: CreatePurpose){
            purposeService.addPurpose(newPurpose)
    }

}