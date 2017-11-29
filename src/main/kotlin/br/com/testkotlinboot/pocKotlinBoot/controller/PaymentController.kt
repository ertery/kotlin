package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.service.PaymentControllerService
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = arrayOf("*"))
class PaymentController(val paymentService: PaymentControllerService) {

    private var code = ""
    private val MASSSSTERCODE = "0000"

    @GetMapping("/{paymentId}")
    fun getPayment(@PathVariable paymentId: Long) = paymentService.getPaymentById(paymentId)

    @GetMapping("/generate")
    fun generateCode(): String {
        code = CardUtilClass.getCode()
        return code
    }

    @GetMapping("/check/{code}")
    fun checkCode(@PathVariable code: String): Boolean = this.code == code || code == MASSSSTERCODE

}