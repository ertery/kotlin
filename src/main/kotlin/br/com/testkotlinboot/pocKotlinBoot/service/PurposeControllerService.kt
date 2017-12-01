package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.dto.*
import br.com.testkotlinboot.pocKotlinBoot.entity.*
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource


@Service("proxy")

class PurposeControllerService(val purposeRepository: PurposeRepository, val personRepository: PersonRepository) {

    val LOGGER = LoggerFactory.getLogger(PurposeControllerService::class.java.name)

    @Resource(name = "proxy")
    lateinit var selfRef: PurposeControllerService

    fun getPurposes(): Any {
        val findAll = purposeRepository.findAll()
        val records: MutableList<PurposeRecord> = mutableListOf()
        findAll.forEach { purpose -> records.add(purpose.toDTO(false)) }
        return records
    }

    fun getPurposeById(id: Long): Any {
        val findOne = purposeRepository.findOne(id)
        return findOne.toDTO(true)
    }

    fun findPurposeByName(name: String): Any {
        val findByName = purposeRepository.findByName(name)
        return findByName.toDTO(true)
    }

    @Transactional
    fun addPurpose(purpose: CreatePurpose): Any {
        val createPurpose = Purpose(name = purpose.name,
                targetAmmount = purpose.targetAmmount,
                finishDate = purpose.finishDate,
                imageUrl = purpose.imageUrl,
                description = purpose.description,
                initiatorId = purpose.initiatorId)

        val forSave: MutableList<Person> = mutableListOf()

        val newPurpose = selfRef.savePurpose(createPurpose)!!

        val initPerson = personRepository.findOne(purpose.initiatorId)

        if (initPerson != null) {
            val pp = PurposePerson(newPurpose, initPerson)
            pp.purposeState = PersonPurposeState.INITIAL
            initPerson.purposes.add(pp)
            forSave.add(initPerson)
            LOGGER.info("Person with id ${initPerson.personId} will be init person for new purpose with name ${purpose.name}")
        }

        purpose.persons.forEach {
            val existPerson = personRepository.findByPhoneNumber(PhoneUtilClass.format(it.phoneNumber))
            if (existPerson != null) {
                val pp = PurposePerson(newPurpose, existPerson)
                existPerson.purposes.add(pp)
                LOGGER.info("Person with such phone number ${it.phoneNumber} already present in db")
                forSave.add(existPerson)
            } else {
                val person = Person(name = it.name, phoneNumber = PhoneUtilClass.format(it.phoneNumber))
                if (!forSave.any { p -> p.phoneNumber == person.phoneNumber }) {
                    val pp = PurposePerson(newPurpose, person)
                    val paymentCard = PaymentCard()
                    paymentCard.person = person
                    person.paymentCard = paymentCard
                    person.purposes.add(pp)
                    forSave.add(person)
                    LOGGER.info("Person with phone number ${PhoneUtilClass.format(it.phoneNumber)} was successfully added for purpose ${purpose.name}")
                }
            }
        }

        val savedPurpose = purposeRepository.saveAndFlush(newPurpose)
        val savedPersons = personRepository.save(forSave)

        return mutableListOf(SavePurposeResponse(savedPurpose.purposeId, savedPersons.map { sp -> SavedPerson(sp.personId, PhoneUtilClass.format(sp.phoneNumber)) } as MutableList<SavedPerson>))
    }

    @Transactional
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
                person.purposes.add(pp)
                LOGGER.info("Person with id ${person.personId} was successfully join to purpose ${purpose.name}")
            } else {
                val personSave = Person(name = it.name, phoneNumber = PhoneUtilClass.format(it.phoneNumber))
                if (!forSave.any { p -> p.phoneNumber == personSave.phoneNumber }) {
                    val pp = PurposePerson(purpose, personSave)
                    val paymentCard = PaymentCard()
                    paymentCard.person = personSave
                    personSave.paymentCard = paymentCard
                    personSave.purposes.add(pp)
                    forSave.add(personSave)
                    LOGGER.info("Person with phone number ${PhoneUtilClass.format(it.phoneNumber)} was successfully join to purpose ${purpose.name}")
                }
            }
        }

        purposeRepository.save(purpose)
        personRepository.save(forSave)

        return 0
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun savePurpose(purpose: Purpose): Purpose? {
        return purposeRepository.saveAndFlush(purpose)
    }
}