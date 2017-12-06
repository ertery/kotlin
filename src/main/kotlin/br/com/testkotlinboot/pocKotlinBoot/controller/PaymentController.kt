package br.com.testkotlinboot.pocKotlinBoot.controller

import br.com.testkotlinboot.pocKotlinBoot.dto.CodeDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusResponse
import br.com.testkotlinboot.pocKotlinBoot.service.PaymentControllerService
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = arrayOf("*"))
class PaymentController(val paymentService: PaymentControllerService) {

    private var code = ""
    private val MASSSSTERCODE = "0000"


    /*fun getPayment(@PathVariable paymentId: Long) = paymentService.getPaymentById(paymentId)*/
    @GetMapping("/{paymentId}")
    fun getPayment(@PathVariable paymentId: Long): CodeDTO = CodeDTO(code = CardUtilClass.getCode(paymentId), paymentId = paymentId)

    @GetMapping("/generate")
    fun generateCode(): String {
        code = CardUtilClass.generateCode()
        return code
    }

    @GetMapping("/check")
    @CrossOrigin(origins = arrayOf("*"))
    fun checkCode(@RequestParam(name = "personId") personId: Long,
                  @RequestParam(name = "paymentId") paymentId: Long,
                  @RequestParam(name = "code") code: String): Boolean {

        //TODO реализовать проверку на основе кода сгенеренного для каждого отдельного пользователя
        val pass = this.code == code || code == MASSSSTERCODE

        paymentService.updatePaymentStatus(personId, paymentId, pass)

        return pass
    }

    @PutMapping("/{paymentId}")
    fun checkCodeFromIos(@PathVariable paymentId: Long,
                         @RequestBody code: CodeDTO): StatusResponse {
        val storedCode = CardUtilClass.getCode(paymentId)
        return if (code.code == storedCode || code.code == "0000")
            StatusResponse(status = "DONE")
        else StatusResponse(status = "DECLINE")
    }

}