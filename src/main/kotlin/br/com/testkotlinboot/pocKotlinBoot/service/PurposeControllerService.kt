package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.dto.CreatePurpose
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.entity.Purpose
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
@Transactional
class PurposeControllerService(val repository: PurposeRepository, val personRepository: PersonRepository) {

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
        val persons: MutableList<Person> = purpose.persons.map { (name, phoneNumber) ->
            Person(name = name, phoneNumber = phoneNumber,
                    purposes = mutableListOf(Purpose(name = purpose.name, targetAmmount = purpose.targetAmount,
                            description = purpose.description, initiatorId = purpose.initiatorId, finishDate = LocalDateTime.now())))
        } as MutableList<Person>
        personRepository.save(persons)
        personRepository.flush()

        return mutableListOf(persons)
    }
}