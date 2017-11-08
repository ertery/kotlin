package br.com.testkotlinboot.pocKotlinBoot.service

import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class PersonControllerService(val repository: PersonRepository) {


    fun findByPersonId(id: Long, isInitial: Boolean): Any {
        val person: Person = repository.findOne(id)
        return if (!isInitial)
            person.purposes
        else person.purposes.filter { purpose -> purpose.initiatorId == id }
    }
}