package br.com.testkotlinboot.pocKotlinBoot.utils

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "service.api")
data class ServiceValues(var key:String = "",
                         var smsLogin: String = "",
                         var smsPass: String = "") {
}