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
    fun addPurpose(@RequestBody newPurpose: Any){
        val create: CreatePurpose = CreatePurpose( name = "Бабахам", targetAmount = 1000.50,
                imageUrl = "tukituki", description = "Первый блин комом", initiatorId = 2,
                persons = mutableListOf(UnregisteredPerson("Трампушка", "88005553535"),
                        UnregisteredPerson("Путин Хуютин", phoneNumber = "333221")))
            purposeService.addPurpose(create)
    }

}