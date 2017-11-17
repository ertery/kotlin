package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.dto.*
import br.com.testkotlinboot.pocKotlinBoot.entity.*
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposePersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class PurposeControllerService(val purposeRepository: PurposeRepository, val personRepository: PersonRepository) {

    val LOGGER = LoggerFactory.getLogger(PurposeControllerService::class.java.name)

    fun getPurposes(): Any {
        val findAll = purposeRepository.findAll()
        val records: MutableList<PurposeRecord> = mutableListOf()
        findAll.forEach { purpose -> records.add(purpose.toDTO()) }
        return records
    }

    fun getPurposeById(id: Long): Any {
        val findOne = purposeRepository.findOne(id)
        return findOne.toDTO()
    }

    fun findPurposeByName(name: String): Any {
        val findByName = purposeRepository.findByName(name)
        return findByName?.toDTO()
    }

    fun addPurpose(purpose: CreatePurpose): Any {
        val newPurpose = Purpose(name = purpose.name,
                targetAmmount = purpose.targetAmmount,
                finishDate = purpose.finishDate,
                imageUrl = purpose.imageUrl,
                description = purpose.description,
                initiatorId = purpose.initiatorId)

        val forSave: MutableList<Person> = mutableListOf()
        val notSaved: MutableList<Person> = mutableListOf()

        purpose.persons.forEach {
            if (personRepository.findByPhoneNumber(PhoneUtilClass.format(it.phoneNumber)) != null) {
                LOGGER.error("Person with such phone number {} already exists", it.phoneNumber)
                notSaved.add(Person(name = it.name, phoneNumber = PhoneUtilClass.format(it.phoneNumber)))
            } else {
                val person = Person(name = it.name, phoneNumber = PhoneUtilClass.format(it.phoneNumber))
                val pp = PurposePerson(newPurpose, person)
                val paymentCard = PaymentCard()
                paymentCard.person = person
                person.paymentCard = paymentCard
                pp.purposeState = PersonPurposeState.INITIAL
                person.purposes.add(pp)
                forSave.add(person)
                LOGGER.info("Person with phone number {} was successfully added for purpose {}", PhoneUtilClass.format(it.phoneNumber), purpose.name)
            }
        }

        val savedPurpose = purposeRepository.save(newPurpose)
        val savedPersons = personRepository.save(forSave)

        return mutableListOf(SavePurposeResponse(savedPurpose.purposeId, savedPersons.map { sp -> SavedPerson(sp.personId, PhoneUtilClass.format(sp.phoneNumber)) } as MutableList<SavedPerson>))
    }

    fun addPersonsToPurpose(purposeId: Long, addedPersons: MutableList<UnregisteredPerson>): Long {

        val purpose = purposeRepository.findOne(purposeId)
        if (purpose == null) {
            LOGGER.error("There are no purpose with id $purposeId")
            return -1
        }
        val updateList = purpose.persons.filter { (purpose1) ->
            purpose1.purposeId == purposeId
        }

        if (updateList.size > 1) {
            LOGGER.error("There are multiple purposes with id $purposeId")
            return -1
        }

        val forSave: MutableList<Person> = mutableListOf()

        addedPersons.forEach {
            val person = personRepository.findByPhoneNumber(PhoneUtilClass.format(it.phoneNumber))
            if (person != null) {
                val pp = PurposePerson(purpose, person)
                pp.purposeState = PersonPurposeState.INITIAL
                person.purposes.add(pp)
                LOGGER.info("Person with id ${person.personId} was successfully join to purpose ${purpose.name}")
            } else {
                val personSave = Person(name = it.name, phoneNumber = PhoneUtilClass.format(it.phoneNumber))
                val pp = PurposePerson(purpose, personSave)
                val paymentCard = PaymentCard()
                paymentCard.person = personSave
                personSave.paymentCard = paymentCard
                pp.purposeState = PersonPurposeState.INITIAL
                personSave.purposes.add(pp)
                forSave.add(personSave)
                LOGGER.info("Person with phone number {} was successfully join to purpose ${purpose.name}", PhoneUtilClass.format(it.phoneNumber))
            }
        }

        purposeRepository.save(purpose)
        personRepository.save(forSave)

        return 0
    }
}