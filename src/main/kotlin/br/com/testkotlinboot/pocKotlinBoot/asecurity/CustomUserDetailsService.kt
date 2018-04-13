package br.com.testkotlinboot.pocKotlinBoot.asecurity

import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(val personRepository: PersonRepository): UserDetailsService {

    override fun loadUserByUsername(phone: String?): UserDetails {
        val person  = personRepository.findByPhoneNumber(phone!!)
        return CustomSecurityUser(person!!.personId,  person!!.phoneNumber)
    }
}