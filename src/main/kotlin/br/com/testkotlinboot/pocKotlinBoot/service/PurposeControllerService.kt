package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class PurposeControllerService(val repository: PurposeRepository) {

    fun getPurposes(): Any {
        val findAll = repository.findAll()
        val records: MutableList<PurposeRecord> = mutableListOf()
        findAll.forEach { purpose -> records.add(purpose.toDTO()) }
        return findAll
    }

    fun getPurposeById(id: Long): Any {
        val findOne = repository.findOne(id)
        return findOne.toDTO()
    }

    fun getPurposeByName(name: String) : Any {
        val findByName = repository.findByName(name)
        return findByName.toDTO()
    }
}