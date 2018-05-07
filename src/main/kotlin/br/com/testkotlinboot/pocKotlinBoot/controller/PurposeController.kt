package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CodeDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePurpose
import br.com.testkotlinboot.pocKotlinBoot.dto.PaymentDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.UnregisteredPerson
import br.com.testkotlinboot.pocKotlinBoot.service.AuthService
import br.com.testkotlinboot.pocKotlinBoot.service.PaymentControllerService
import br.com.testkotlinboot.pocKotlinBoot.service.PurposeControllerService
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/purpose")
class PurposeController(val purposeService: PurposeControllerService,
                        val authService: AuthService,
                        val paymentService: PaymentControllerService) {


    @RequestMapping("/")
    fun getPurposes() = purposeService.getPurposes()

    @GetMapping("/{id}")
    fun getPurposeById(@PathVariable id: Long) = purposeService.getPurposeById(id)

    @GetMapping("/find")
    fun getPurposeByName(
            @RequestParam(value = "byName", required = false) name: String?,
            @RequestParam(value = "state", required = false) state: String?,
            @RequestHeader(value = "Authorization", required = false) authorization: String?): ResponseEntity<Any> {
        if (authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val id = authService.decodeToken(authorization!!)
        return when {
            name != null -> ResponseEntity(purposeService.findPurposeByName(name), HttpStatus.OK)
            id != null && state != null -> ResponseEntity(purposeService.findByPersonIdAndState(id, state, false), HttpStatus.OK)
            id != null -> ResponseEntity(purposeService.findByPersonIdAndState(id, "ACCEPT", true), HttpStatus.OK)
            else -> ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/")
    fun addPurpose(@RequestBody newPurpose: CreatePurpose,
                   @RequestHeader(value = "Authorization", required = false) authorization: String?): Any {
        if (authorization.isNullOrBlank()) {
            return ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST)
        }
        purposeService.addPurpose(newPurpose, authorization)
        return HttpStatus.OK
    }

    @PostMapping("/{id}/person")
    fun addPersonsToPurpose(@PathVariable id: Long,
                            @RequestBody addedPersons: MutableList<UnregisteredPerson>) {
        purposeService.addPersonsToPurpose(id, addedPersons)
    }

    @PostMapping("{purposeId}/payment")
    fun receivePayment(@PathVariable purposeId: Long,
                       @RequestBody newPayment: PaymentDTO,
                       @RequestHeader("Authorization", required = false) authorization: String?): ResponseEntity<CodeDTO> {
        if (authorization.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        paymentService.createNewPayment(purposeId, authorization!!, newPayment)?.let {
            val code = CardUtilClass.generateCode()
            CardUtilClass.storeCode(it.id, code)
            paymentService.sendCodeByPush(it.id)
            return ResponseEntity(CodeDTO(it.id, code), HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}