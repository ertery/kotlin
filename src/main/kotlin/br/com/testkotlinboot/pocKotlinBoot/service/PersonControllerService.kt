package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.dto.*
import br.com.testkotlinboot.pocKotlinBoot.entity.Device
import br.com.testkotlinboot.pocKotlinBoot.entity.PaymentCard
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.entity.PurposePerson
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.DeviceInfoRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import br.com.testkotlinboot.pocKotlinBoot.utils.ServiceValues
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.impl.DefaultClaims
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class PersonControllerService(
        val personRepository: PersonRepository,
        val purposeRepository: PurposeRepository,
        val deviceInfoRepository: DeviceInfoRepository,
        val authService: AuthService,
        val values: ServiceValues
) {


    private val LOGGER = LoggerFactory.getLogger(PersonControllerService::class.java)


    @Transactional
    fun findByPersonId(id: Long): Any {
        LOGGER.info("Get purposes by person id: $id")
        val person: Person = personRepository.findOne(id)
        val purposes: MutableList<PurposeRecord> = mutableListOf()
        person.purposes.filter { it -> !it.purpose.archived }.forEach { (purpose) ->
            val record: PurposeRecord = purpose.toDTO(true)
            record.isInitial = id == purpose.initiatorId
            purposes.add(record)
        }
        return purposes
    }

    @Transactional
    fun findPersonByFacebookId(facebookId: String): Any {
        LOGGER.info("Get purposes by facebookId: $facebookId")
        val person = personRepository.findByFacebookId(facebookId) ?: return -1

        val response = PersonDTO(name = person.name, phoneNumber = person.phoneNumber, imagePath = person.imagePath,
                email = person.email, facebookId = person.facebookId, id = person.personId)
        if (person.paymentCard != null) {
            response.paymentCard = CardDTO(number = person.paymentCard!!.cardNumber, cardholderName = person.paymentCard!!.cardholderName,
                    term = person.paymentCard!!.validity?.format(DateTimeFormatter.ofPattern("yyyy-dd-MM")), id = person.paymentCard!!.id)
        }
        return response
    }

    @Transactional
    fun updateState(status: StatusUpdate, authorization: String) {
        val person = personRepository.findOne(authService.decodeToken(authorization))
        person.purposes.filter { (purpose) -> purpose.purposeId == status.purposeId }.forEach { it.purposeState = PersonPurposeState.valueOf(status.state.toUpperCase()) }
        personRepository.save(person)
    }

    fun createOrUpdatePerson(createPerson: PersonDTO): Long {
        val personByFacebookId = personRepository.findByPhoneNumber(PhoneUtilClass.format(createPerson.phoneNumber))
        val personByPhone = personRepository.findByFacebookId(createPerson.facebookId)

        if (personByFacebookId != null || personByPhone != null) {
            val person = if (personByFacebookId != null && personByPhone != null) personByPhone
            else personByFacebookId ?: personByPhone

            person?.name = createPerson.name
            person?.facebookId = createPerson.facebookId
            person?.imagePath = createPerson.imagePath
            person?.email = createPerson.email

            personRepository.saveAndFlush(person)

            return 0
        }

        val person = Person(name = createPerson.name, imagePath = createPerson.imagePath, email = createPerson.email,
                facebookId = createPerson.facebookId, phoneNumber = PhoneUtilClass.format(createPerson.phoneNumber))
        val savedPerson = personRepository.save(person)

        return savedPerson.personId
    }

    fun addCard(personId: Long, card: CardDTO): Long? {
        val person = personRepository.findOne(personId) ?: return -1

        val formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM")

        if (person.paymentCard != null) return -1

        val newCard = PaymentCard(validity = LocalDate.parse(card.term, formatter), cardNumber = card.number,
                cardholderName = card.cardholderName)
        person.paymentCard = newCard
        newCard.person = person

        return personRepository.save(person).paymentCard?.id
    }

    @Transactional
    fun addState(state: StatusUpdate): Any {
        val person = personRepository.findOne(state.personId)
        val purpose = purposeRepository.findOne(state.purposeId)

        if (person == null || purpose == null) return -1

        val purposePerson = PurposePerson(purpose, person)
        purposePerson.purposeState = PersonPurposeState.valueOf(state.state.toUpperCase())

        person.purposes.add(purposePerson)

        purposeRepository.saveAndFlush(purpose)
        personRepository.saveAndFlush(person)


        return 0
    }

    fun updateCard(id: Long, card: CardDTO): Long? {
        val person = personRepository.findOne(id) ?: return -1

        val formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM")
        val paymentCard = person.paymentCard

        if (paymentCard != null) {
            paymentCard.cardNumber = card.number
            paymentCard.cardholderName = card.cardholderName
            paymentCard.validity = LocalDate.parse(card.term, formatter)
        } else return -1

        return personRepository.save(person).paymentCard?.id
    }

    @Transactional
    fun addToken(token: TokenDTO, authorization: String) {
        val person = personRepository.findOne(authService.decodeToken(authorization))

        val device = Device(id = null, token = token.token)

        if (person != null && !person.devices.any { it.token == token.token }) {
            device.person = person
            val savedDevice = deviceInfoRepository.saveAndFlush(device)
            person.devices.add(savedDevice)
        }

        personRepository.saveAndFlush(person)
    }

    fun togglePerson(toggle: YandexToggleDTO, token: String?): Boolean {
        val key = values.key
        val claims = Jwts.parser().setSigningKey(key).parse(token).body as DefaultClaims
        val personId = claims.get("userID", Integer::class.java) ?: return false
        val person = personRepository.findOne(personId.toLong()) ?: return false
        person.yandexConnected = toggle.connect
        personRepository.save(person)
        return true
    }

    @Transactional
    fun updatePerson(person: PersonModifyDTO, authorization: String?) {
        val personId = authService.decodeToken(authorization!!)
        val existedPerson = personRepository.findOne(personId)
        person.name?.let { existedPerson.name = it }
        person.imagePath?.let { existedPerson.imagePath = it }

        personRepository.save(existedPerson)
    }
}