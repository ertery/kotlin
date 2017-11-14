package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.dto.StatusUpdate
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.enums.PersonPurposeState
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PersonControllerService(val repository: PersonRepository) {


    @Transactional
    fun findByPersonId(id: Long): Any {
        val person: Person = repository.findOne(id)
        val purposes: MutableList<PurposeRecord> = mutableListOf()
        person.purposes.forEach { pp ->
            val record: PurposeRecord = pp.purpose.toDTO()
            record.isInitial = id == pp.purpose.initiatorId
            purposes.add(record)
        }
        return purposes
    }

    @Transactional
    fun updateStatus(status: StatusUpdate) {
        val person = repository.findOne(status.personId)
        person.purposes.filter { (purpose) -> purpose.purposeId == status.purposeId }.
                forEach { it.purposeState = PersonPurposeState.valueOf(status.state.toUpperCase()) }
        repository.save(person)
    }


}