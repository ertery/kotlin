package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.dto.*
import br.com.testkotlinboot.pocKotlinBoot.entity.PaymentCard
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.entity.Purpose
import br.com.testkotlinboot.pocKotlinBoot.entity.PurposePerson
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import br.com.testkotlinboot.pocKotlinBoot.utils.PhoneUtilClass
import br.com.testkotlinboot.pocKotlinBoot.utils.SMSender
import br.com.testkotlinboot.pocKotlinBoot.utils.ServiceValues
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.annotation.Resource


@Service("proxy")
class PurposeControllerService(val purposeRepository: PurposeRepository,
                               val personRepository: PersonRepository,
                               val authService: AuthService,
                               val sender: SMSender,
                               val values: ServiceValues) {

    val LOGGER = LoggerFactory.getLogger(PurposeControllerService::class.java.name)

    val PUSH_TITLE_INVITE = "Новое приглашение"

    @Resource(name = "proxy")
    lateinit var selfRef: PurposeControllerService

    fun getPurposes(authorization: String?): Any {
        val findAll = purposeRepository.findAll().filter { it -> !it.archived }
        val user = authService.decodeToken(authorization!!)
        val records: MutableList<PurposeRecord> = mutableListOf()
        findAll.filter { it -> it.persons.filter { it1 -> it1.person.personId == user }.count() == 1 }.forEach { purpose -> records.add(purpose.toDTO(false)) }
        return records
    }

    fun getPurposeById(id: Long): Any {
        val findOne = purposeRepository.findOne(id)
        return findOne.toDTO(true)
    }

    fun findPurposeByName(name: String): Any? {
        val findByName = purposeRepository.findByName(name)
        return if (findByName.archived) {
            null
        } else findByName.toDTO(true)
    }

    @Transactional
    fun addPurpose(purpose: CreatePurpose, authorization: String?): Any? {
        val initiatorId = authService.decodeToken(authorization!!) ?: return null
        val createPurpose = Purpose(name = purpose.name,
                targetAmmount = purpose.targetAmmount,
                finishDate = LocalDateTime.of(purpose.finishDate, LocalTime.now(ZoneId.of("Europe/Moscow"))),
                imageUrl = purpose.imageUrl,
                description = purpose.description,
                initiatorId = initiatorId)

        val forSave: MutableList<Person> = mutableListOf()

        val newPurpose = selfRef.savePurpose(createPurpose)

        val initPerson = personRepository.findOne(initiatorId)

        if (initPerson != null) {
            val pp = PurposePerson(newPurpose, initPerson)
            pp.purposeState = PersonPurposeState.INITIAL
            initPerson.purposes.add(pp)
            forSave.add(initPerson)
            LOGGER.info("Person with id ${initPerson.personId} will be init person for new purpose with name ${purpose.name}")
        }

        purpose.persons.forEach {
            val existPerson = personRepository.findByPhoneNumber(PhoneUtilClass.format(it.phoneNumber))
            if (existPerson != null && !forSave.contains(existPerson)) {
                LOGGER.info("Person with such phone number ${it.phoneNumber} already present in db")
                val pp = PurposePerson(newPurpose, existPerson)
                existPerson.purposes.add(pp)
                forSave.add(existPerson)
            } else {
                val person = Person(name = it.name!!, phoneNumber = PhoneUtilClass.format(it.phoneNumber))
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

        forSave.filter { person -> !person.devices.isEmpty() && person.personId != savedPurpose.initiatorId }.forEach { person ->
            person.devices.forEach { device ->
                CardUtilClass.sendPush(device.token, PUSH_TITLE_INVITE, "Вас пригласили в кампанию ${newPurpose.name}")
            }
        }

        return mutableListOf(SavePurposeResponse(savedPurpose.purposeId, savedPersons.map { sp -> SavedPerson(sp.personId, PhoneUtilClass.format(sp.phoneNumber)) } as MutableList<SavedPerson>))
    }

    @Transactional
    fun addPersonsToPurpose(purposeId: Long, addedPersons: MutableList<UnregisteredPerson>): Long {

        val purpose = purposeRepository.findOne(purposeId)

        if (purpose == null) {
            LOGGER.error("There are no purpose with id $purposeId")
            return -1
        }

        val forSave: MutableList<Person> = mutableListOf()

        addedPersons.forEach {
            val formattedPhone = PhoneUtilClass.format(it.phoneNumber);
            val person = personRepository.findByPhoneNumber(formattedPhone)
            if (person != null) {
                val pp = PurposePerson(purpose, person)
                if (!purpose.persons.contains(pp)) {
                    person.purposes.add(pp)
                    purposeRepository.saveAndFlush(purpose)
                    personRepository.saveAndFlush(person)
                    if (person.devices.size > 0) {
                        person.devices.forEach { device ->
                            CardUtilClass.sendPush(device.token, PUSH_TITLE_INVITE, "Вас пригласили в кампанию ${purpose.name}")
                        }
                    } else {
                        sendWelcomeToNewPurposeSms(formattedPhone, purpose.name)
                    }
                    LOGGER.info("Person with id ${person.personId} was successfully invited for purpose ${purpose.name}")
                }
            } else {
                val personSave = Person(name = it.name!!, phoneNumber = PhoneUtilClass.format(it.phoneNumber))
                if (!forSave.any { p -> p.phoneNumber == personSave.phoneNumber }) {
                    val pp = PurposePerson(purpose, personSave)
                    personSave.purposes.add(pp)
                    forSave.add(personSave)
                    sendWelcomeToNewPurposeSms(formattedPhone, purpose.name)
                    LOGGER.info("Person with phone number $formattedPhone successfully join to purpose ${purpose.name}")
                }
            }
        }

        purposeRepository.save(purpose)
        personRepository.save(forSave)

        return 0
    }

    @Transactional
    fun sendWelcomeToNewPurposeSms(phone: String, companyName: String) {
        val text = "Вас пригласили в кампанию $companyName"
        val messages: Array<String> = runBlocking {
            val l = async { send(phone, text) }
            l.await()
        }

        if (Integer.valueOf(messages[1]) == 1) {
            LOGGER.info("Sms with invite to $companyName was successfully send to 7$phone")
        } else {
            LOGGER.info("Sms with invite to $companyName was't delivered to the  7$phone")
        }
    }

    suspend fun send(phone: String, text: String): Array<String> = sender
            .sendSms("7$phone", text, values.fake == "fake")


    @Transactional
    fun findByPersonIdAndState(personId: Long, state: String, applySecondCondition: Boolean): Any {
        LOGGER.info("Get purposes by person id: $personId")
        val person: Person = personRepository.findOne(personId) ?: return arrayListOf<String>()

        try {
            PersonPurposeState.valueOf(state.toUpperCase())
        } catch (e: Exception) {
            return arrayListOf<String>()
        }

        val purposes: MutableList<PurposeRecord> = mutableListOf()
        person.purposes
                .filter { it -> (it.purposeState == PersonPurposeState.valueOf(state.toUpperCase()) || (applySecondCondition && it.purposeState == PersonPurposeState.INITIAL)) }
                .forEach { (purpose) ->
                    if (!purpose.archived) {
                        val record: PurposeRecord = purpose.toDTO(true)
                        record.isInitial = personId == purpose.initiatorId
                        purposes.add(record)
                    }
                }
        return purposes
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun savePurpose(purpose: Purpose): Purpose = purposeRepository.saveAndFlush(purpose)

    @Transactional
    fun deletePurpose(purposeId: Long, authorization: String?): Boolean {
        val purpose = purposeRepository.getOne(purposeId)
        purpose?.let {
            val initPerson = purpose.persons.filter { it.purposeState == PersonPurposeState.INITIAL }[0]
            if (initPerson.person.personId != authService.decodeToken(authorization!!)) {
                return false
            }
            purpose.archived = true
            purposeRepository.save(purpose)
            return true
        }
        return false
    }

    @Transactional
    fun modifyPurpose(authorization: String?, purposeModify: PurposeModifyDTO, purposeId: Long): Boolean {
        val purpose = purposeRepository.getOne(purposeId)
        val userId = authService.decodeToken(authorization!!)
        if (purpose.persons.filter { it -> it.person.personId == userId && it.purposeState == PersonPurposeState.INITIAL }.size != 1) {
            return false
        }
        purposeModify.description?.let { purpose.description = it }
        purposeModify.finishDate?.let { purpose.finishDate = it.atTime(23, 59) }
        purposeModify.imageUrl?.let { purpose.imageUrl = it }
        purposeModify.targetAmmount?.let { purpose.targetAmmount = it }
        purposeModify.name?.let { purpose.name = it }

        purposeRepository.save(purpose)

        return true
    }
}