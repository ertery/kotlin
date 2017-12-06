package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CodeDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePurpose
import br.com.testkotlinboot.pocKotlinBoot.dto.PaymentDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.UnregisteredPerson
import br.com.testkotlinboot.pocKotlinBoot.service.PaymentControllerService
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import br.com.testkotlinboot.pocKotlinBoot.service.PurposeControllerService
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/purpose")
class PurposeController(val purposeService: PurposeControllerService,
                        val personService: PersonControllerService,
                        val paymentService: PaymentControllerService) {


    @RequestMapping("/")
    fun getPurposes() = purposeService.getPurposes()

    @GetMapping("/{id}")
    fun getPurposeById(@PathVariable id: Long) = purposeService.getPurposeById(id)

    @GetMapping("/find")
    fun getPurposeByName(@RequestParam(value = "byName", required = false) name: String?,
                         @RequestParam(value = "byId", required = false) id: Long?): Any = when {
                             name != null -> purposeService.findPurposeByName(name)
                             id != null -> personService.findByPersonId(id)
                             else -> mutableListOf<String>()
                         }

    @PostMapping("/")
    fun addPurpose(@RequestBody newPurpose: CreatePurpose): Any = purposeService.addPurpose(newPurpose)

    @PostMapping("/{id}/person")
    fun addPersonsToPurpose(@PathVariable id: Long,
                            @RequestBody addedPersons: MutableList<UnregisteredPerson>){
            purposeService.addPersonsToPurpose(id, addedPersons)
    }

    @PostMapping("{purposeId}/person/{personId}/payment")
    fun receivePayment(@PathVariable purposeId: Long,
                       @PathVariable personId: Long,
                       @RequestBody newPayment: PaymentDTO): CodeDTO {
        val paymentId = paymentService.createNewPayment(purposeId, personId, newPayment)?.id
        val code =  CardUtilClass.generateCode()
        if (paymentId != null) CardUtilClass.storeCode(paymentId, code)
        return CodeDTO(paymentId, code)
    }
}