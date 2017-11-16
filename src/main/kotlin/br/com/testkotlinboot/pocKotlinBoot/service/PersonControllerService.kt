package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePerson
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusUpdate
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PersonControllerService(val personRepository: PersonRepository) {


    private val LOGGER = LoggerFactory.getLogger(PersonControllerService::class.java)


    @Transactional
    fun findByPersonId(id: Long): Any {
        LOGGER.info("Get purposes by person id: $id")
        val person: Person = personRepository.findOne(id)
        val purposes: MutableList<PurposeRecord> = mutableListOf()
        person.purposes.forEach { (purpose) ->
            val record: PurposeRecord = purpose.toDTO()
            record.isInitial = id == purpose.initiatorId
            purposes.add(record)
        }
        LOGGER.info("SUCCESSFUL: Purposes were returned : {}", purposes)
        return purposes
    }

    @Transactional
    fun updateStatus(status: StatusUpdate) {
        val person = personRepository.findOne(status.personId)
        person.purposes.filter { (purpose) -> purpose.purposeId == status.purposeId }.
                forEach { it.purposeState = PersonPurposeState.valueOf(status.state.toUpperCase()) }
        personRepository.save(person)
    }

    fun createPerson(createPerson: CreatePerson): Long {
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


}