package br.com.testkotlinboot.pocKotlinBoot.asecurity

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter : AbstractAuthenticationProcessingFilter {

    constructor() : super("/person/**") {
        setAuthenticationSuccessHandler { request, response, authentication ->
            SecurityContextHolder.getContext().authentication = authentication
            request.getRequestDispatcher(request.servletPath + request.pathInfo).forward(request, response)
        }
        setAuthenticationFailureHandler { request, response, authenticationException -> response.outputStream.print(authenticationException.message) }
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        var token = request.getHeader("token")
        if (token == null) {
            token = request.getHeader("Authorization")
        }
        if (token == null) {
            token = request.getParameter("token")
        }
        if (token == null) {
            val authentication = TokenAuthentication(null)
            authentication.isAuthenticated = false
            return authentication
        }
        val tokenAuthentication = TokenAuthentication(token)
        return authenticationManager.authenticate(tokenAuthentication)
    }

    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
        val authentication = attemptAuthentication(req as HttpServletRequest, res as HttpServletResponse)
        SecurityContextHolder.getContext().authentication = authentication
        chain!!.doFilter(req, res)
    }
}