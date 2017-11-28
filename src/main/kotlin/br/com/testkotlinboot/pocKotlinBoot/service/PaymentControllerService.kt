package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.dto.PaymentDTO
import br.com.testkotlinboot.pocKotlinBoot.entity.Payment
import br.com.testkotlinboot.pocKotlinBoot.enums.Channel
import br.com.testkotlinboot.pocKotlinBoot.enums.PaymentMethod
import br.com.testkotlinboot.pocKotlinBoot.repository.PaymentRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional



@Service
@Transactional
class PaymentControllerService(val paymentRepository: PaymentRepository,val  personRepository: PersonRepository,val purposeRepository: PurposeRepository) {


    fun createNewPayment(purposeId: Long, personId: Long, newPayment: PaymentDTO): Payment? {

        val payment = Payment(amount = newPayment.ammount)
        payment.paymentMethod = PaymentMethod.valueOf(newPayment.paymentMethod.toUpperCase())
        payment.channel = Channel.valueOf(newPayment.channel.toUpperCase())

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
}