package br.com.testkotlinboot.pocKotlinBoot.asecurity

import br.com.testkotlinboot.pocKotlinBoot.utils.ServiceValues
import org.springframework.security.authentication.AuthenticationServiceException
import io.jsonwebtoken.impl.DefaultClaims
import io.jsonwebtoken.Jwts
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*


@Service
class TokenAuthenticationManager(val userDetailsService: UserDetailsService,val values: ServiceValues) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication {
        return if (authentication is TokenAuthentication) {
            processAuthentication(authentication)
        } else {
            authentication.isAuthenticated = false
            authentication
        }
    }

    private fun processAuthentication(authentication: TokenAuthentication): TokenAuthentication {
        val token = authentication.token
        val key = values.key
        val claims: DefaultClaims
        try {
            claims = Jwts.parser().setSigningKey(key).parse(token).body as DefaultClaims
        } catch (ex: Exception) {
            throw AuthenticationServiceException("Token corrupted") as Throwable
        }

        if (claims.get("token_expiration_date", Date::class.java) == null)
            throw AuthenticationServiceException("Invalid token")
        val expiredDate = Date(claims.get("token_expiration_date", Date::class.java).time)
        return if (expiredDate.after(Date()))
            buildFullTokenAuthentication(authentication, claims)
        else
            throw AuthenticationServiceException("Token expired date error")
    }

    private fun buildFullTokenAuthentication(authentication: TokenAuthentication,
                                             claims: DefaultClaims): TokenAuthentication {
        val user = userDetailsService.loadUserByUsername(claims.get("username", String::class.java))
        if (user.isEnabled) {
            return TokenAuthentication(authentication.token!!, true, user)
        } else {
            throw AuthenticationServiceException("User disabled")
        }
    }
}