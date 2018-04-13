package br.com.testkotlinboot.pocKotlinBoot.asecurity

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig(val tokenAuthenticationManager: TokenAuthenticationManager) : WebSecurityConfigurerAdapter(false) {


    override fun configure(http: HttpSecurity) {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .addFilterAfter(tokenAuthenticationFilter(), BasicAuthenticationFilter::class.java)
                .authorizeRequests()
                .antMatchers("/person/*").authenticated()
                .and().csrf().disable()
    }

    @Bean(name = arrayOf("restTokenAuthenticationFilter"))
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        val restTokenAuthenticationFilter = TokenAuthenticationFilter()
        restTokenAuthenticationFilter.setAuthenticationManager(tokenAuthenticationManager)
        return restTokenAuthenticationFilter
    }
}