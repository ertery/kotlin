package br.com.testkotlinboot.pocKotlinBoot.asecurity

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class TokenAuthentication : Authentication {
    var token: String? = null
    private var isAuthenticated: Boolean = false
    private var principal: UserDetails? = null

    constructor(token: String?) {
        this.token = token
    }

    constructor(token: String,
                isAuthenticated: Boolean,
                principal: UserDetails) {
        this.token = token
        this.isAuthenticated = isAuthenticated
        this.principal = principal
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any {
        return details
    }

    override fun getName(): String? {
        return principal?.username
    }

    override fun getPrincipal(): Any? {
        return principal
    }

    override fun isAuthenticated(): Boolean {
        return isAuthenticated
    }

    override fun setAuthenticated(b: Boolean) {
        isAuthenticated = b
    }

}