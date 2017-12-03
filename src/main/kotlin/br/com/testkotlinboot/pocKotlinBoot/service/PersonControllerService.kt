package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.dto.CardDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.PersonDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusUpdate
import br.com.testkotlinboot.pocKotlinBoot.entity.PaymentCard
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.entity.PurposePerson
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class PersonControllerService(val personRepository: PersonRepository, val purposeRepository: PurposeRepository) {


    private val LOGGER = LoggerFactory.getLogger(PersonControllerService::class.java)


    @Transactional
    fun findByPersonId(id: Long): Any {
        LOGGER.info("Get purposes by person id: $id")
        val person: Person = personRepository.findOne(id)
        val purposes: MutableList<PurposeRecord> = mutableListOf()
        person.purposes.forEach { (purpose) ->
            val record: PurposeRecord = purpose.toDTO(true)
            record.isInitial = id == purpose.initiatorId
            purposes.add(record)
        }
        LOGGER.info("SUCCESSFUL: Purposes were returned : {}", purposes)
        return purposes
    }

    @Transactional
    fun findPersonByFacebookId(facebookId: String): Any {
        LOGGER.info("Get purposes by facebookId: $facebookId")
        val person = personRepository.findByFacebookId(facebookId)
        return PersonDTO(name = person?.name!!, phoneNumber = person.phoneNumber, imagePath = person.imagePath,
                email = person.email, facebookId = person.facebookId, id = person.personId)
    }

    @Transactional
    fun updateState(status: StatusUpdate) {
        val person = personRepository.findOne(status.personId)
        person.purposes.filter { (purpose) -> purpose.purposeId == status.purposeId }.
                forEach { it.purposeState = PersonPurposeState.valueOf(status.state.toUpperCase()) }
        personRepository.save(person)
    }

    fun createPerson(createPerson: PersonDTO): Long {
        if (personRepository.findByPhoneNumber(PhoneUtilClass.format(createPerson.phoneNumber)) != null ||
                personRepository.findByFacebookId(createPerson.facebookId) != null) {
            LOGGER.error("Person with phone ${createPerson.phoneNumber} and facebookId ${createPerson.facebookId} already exist")
            return -1
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
}