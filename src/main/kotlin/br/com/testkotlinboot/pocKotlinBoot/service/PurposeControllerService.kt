package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePurpose
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.entity.Purpose
import br.com.testkotlinboot.pocKotlinBoot.entity.PurposePerson
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposePersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class PurposeControllerService(val repository: PurposeRepository, val personRepository: PersonRepository, val ppRepository: PurposePersonRepository) {

    fun getPurposes(): Any {
        val findAll = repository.findAll()
        val records: MutableList<PurposeRecord> = mutableListOf()
        findAll.forEach { purpose -> records.add(purpose.toDTO()) }
        return records
    }

    fun getPurposeById(id: Long): Any {
        val findOne = repository.findOne(id)
        return findOne.toDTO()
    }

    fun findPurposeByName(name: String): Any {
        val findByName = repository.findByName(name)
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
        purpose.persons.forEach {
            val person = Person(name = it.name, phoneNumber = it.phoneNumber)
            val pp = PurposePerson(newPurpose, person)
            pp.purposeState = PersonPurposeState.INITIAL
            person.purposes.add(pp)
            forSave.add(person)
        }
        repository.save(newPurpose)
        personRepository.save(forSave)
        personRepository.flush()

        return mutableListOf(forSave)
    }
}