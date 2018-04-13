package br.com.testkotlinboot.pocKotlinBoot.asecurity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomSecurityUser( var id: Long, private  var username: String) : UserDetails {

    private lateinit var authorities: Set<GrantedAuthority>
    private var accountNonExpired: Boolean = true
    private val accountNonLocked: Boolean = true
    private val credentialsNonExpired: Boolean = true
    private val enabled: Boolean = true


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEnabled(): Boolean {
       return enabled//To change body of created functions use File | Settings | File Templates.
    }

    override fun getUsername(): String {
        return  username
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPassword(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}