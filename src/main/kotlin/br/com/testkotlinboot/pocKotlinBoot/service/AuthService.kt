package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.asecurity.CustomUserDetailsService
import br.com.testkotlinboot.pocKotlinBoot.dto.SMSCodeDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.UnregisteredPerson
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.entity.Unregistered
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.RegistrationRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import br.com.testkotlinboot.pocKotlinBoot.utils.SMSender
import org.springframework.stereotype.Service
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*


@Service
class AuthService(private val personRepository: PersonRepository,
                  val registrationRepository: RegistrationRepository,
                  val userDetailsService: CustomUserDetailsService) {

    @Transactional
    fun sendSms(person: UnregisteredPerson): Boolean {

        val formattedPhone = PhoneUtilClass.format(person.phoneNumber)
        val code = CardUtilClass.generateCode()

        val personByFacebookId = personRepository.findByPhoneNumber(formattedPhone)

        var messages: Array<String> = emptyArray()
        if (personByFacebookId == null) {
            registrationRepository.saveAndFlush(Unregistered(phone = formattedPhone, code = code))
            messages = runBlocking {
                val l = async { send(person, code) }
                l.await()
            }
        }
        return Integer.valueOf(messages[1]) == 1
    }

    suspend fun send(person: UnregisteredPerson, code: String): Array<String> = SMSender().sendSms("7" + person.phoneNumber, "Код подтверждения номера: $code", "fake")

    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    fun deleteExpiredRecords() {
        val expiredRecords = registrationRepository.findBySendTimeBefore(now())
        expiredRecords.forEach {
            println(it.phone + " " + it.code + " was deleted")
        }
        registrationRepository.delete(expiredRecords)
    }
    
    private fun now() = LocalDateTime.now(ZoneId.of("Europe/Moscow")).minusMinutes(5)

    fun checkSMSCode(auth: SMSCodeDTO): Boolean {
            val formattedPhone = PhoneUtilClass.format(auth.phone)

            val person = registrationRepository.findByCode(auth.code) ?: return false
            if (person.phone != formattedPhone) return false

            val records = registrationRepository.findByPhone(formattedPhone)
            if (!records.isEmpty()) {
                registrationRepository.delete(records)
            }

            personRepository.saveAndFlush(Person(phoneNumber = auth.phone))

            return true
    }


    fun getToken(username: String?, password: String?): String? {
        if (username == null || password == null)
            return null
        val user = userDetailsService.loadUserByUsername(username) as Person
        val tokenData = HashMap<String, Any>()
            tokenData["clientType"] = "user"
            tokenData["userID"] = user.personId
            tokenData["username"] = user.phoneNumber
            tokenData["token_create_date"] = Date().time
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, 3)
            tokenData["token_expiration_date"] = calendar.time
            val jwtBuilder = Jwts.builder()
            jwtBuilder.setExpiration(calendar.time)
            jwtBuilder.setClaims(tokenData)
            val key = "abc123"
            return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact()
    }
}