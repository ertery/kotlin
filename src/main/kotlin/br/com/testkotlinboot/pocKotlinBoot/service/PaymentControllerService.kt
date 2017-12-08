package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.dto.PaymentDTO
import br.com.testkotlinboot.pocKotlinBoot.entity.Device
import br.com.testkotlinboot.pocKotlinBoot.entity.Payment
import br.com.testkotlinboot.pocKotlinBoot.enums.Channel
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentMethod
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentState
import br.com.testkotlinboot.pocKotlinBoot.repository.PaymentRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class PaymentControllerService(val paymentRepository: PaymentRepository, val personRepository: PersonRepository, val purposeRepository: PurposeRepository) {

    private val LOGGER = LoggerFactory.getLogger(PersonControllerService::class.java)

    fun createNewPayment(purposeId: Long, personId: Long, newPayment: PaymentDTO): Payment? {

        val payment = Payment(amount = newPayment.ammount)
        payment.paymentMethod = PaymentMethod.valueOf(newPayment.paymentMethod.toUpperCase())
        payment.channel = Channel.valueOf(newPayment.channel.toUpperCase())
        payment.state = PaymentState.NEW

        val person = personRepository.findOne(personId)
        val purpose = purposeRepository.findOne(purposeId)

        if (person == null || purpose == null) return Payment(id = 0)

        payment.person = person
        payment.purpose = purpose

        person.payments.add(payment)
        purpose.payments.add(payment)

        return paymentRepository.saveAndFlush(payment)
    }

    fun getPaymentById(paymentId: Long): Any {
        val payment = paymentRepository.findOne(paymentId)
        return payment.toPaymentResponseDTO()
    }

    fun sendCodeByPush(paymentId: Long) {

        val payment: Payment = paymentRepository.getOne(paymentId)
        val devices: MutableList<Device> = payment.person.devices

        devices.forEach { device -> CardUtilClass.sendPush(token = device.token, body = CardUtilClass.getCode(paymentId), title = "Олалала") }

    }

    fun updatePaymentStatus(paymentId: Long, isSuccess: Boolean) {
        val processedPayment = paymentRepository.findOne(paymentId)

        if (processedPayment != null && PaymentState.NEW != processedPayment.state) {
            LOGGER.info("Payment with id $paymentId is not in NEW state, proceed without saving or update")
            return
        }

        if (isSuccess) processedPayment.state = PaymentState.DONE
        else processedPayment.state = PaymentState.DECLINED

        paymentRepository.saveAndFlush(processedPayment)

        val purpose = purposeRepository.findOne(processedPayment.purpose.purposeId)
        var currAmount = 0.0
        purpose.payments.filter { payment -> PaymentState.DONE == payment.state }.forEach { payment -> currAmount += payment.amount }
        purpose.currentAmmount = currAmount

        purposeRepository.saveAndFlush(purpose)
    }
}