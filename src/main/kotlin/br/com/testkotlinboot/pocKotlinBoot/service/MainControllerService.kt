package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class MainControllerService(val repository: PurposeRepository) {

    fun getPurposes(): Any {
        val findAll = repository.findAll()
        println(findAll)
        return findAll
    }

    fun getPurposeById(id: Long) = repository.findOne(id)
    fun getPurposeByName(name: String) = repository.findByName(name)
}