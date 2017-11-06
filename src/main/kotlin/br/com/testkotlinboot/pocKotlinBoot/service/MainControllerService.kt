package br.com.testkotlinboot.pocKotlinBoot.service


import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import org.springframework.stereotype.Service


@Service
class MainControllerService(val repository: PurposeRepository) {

    fun getPurposes() = repository.findAll()
    fun getPurposeById(id: Long) = repository.findOne(id)
    fun getPurposeByName(name : String) = repository.findByName(name)
}