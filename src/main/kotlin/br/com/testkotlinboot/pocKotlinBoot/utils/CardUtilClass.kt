package br.com.testkotlinboot.pocKotlinBoot.utils

import com.notnoop.apns.APNS
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class CardUtilClass {

    companion object {

        @Value("\${password}")
        private val password: String? = null

        private val codes: HashMap<Long, String> = hashMapOf()

        fun storeCode(pId: Long, code: String) {
            codes.put(pId, code)
        }

        fun getCode(pId: Long): String = codes[pId] ?: "Код не найден"

        fun generateCode(): String {
            val sb = StringBuilder()
            for (i in 0 until 4)
                sb.append(Random().nextInt(9))
            return sb.toString()
        }

        fun maskNumber(unmasked: String?): String {
            val re = Regex("\\D")
            if (unmasked == null) return "Платеж не может быть исполнен."
            val r = re.replace(unmasked, "")
            return if (r.length == 16) {
                "**** **** **** " + r.substring(12)
            } else r
        }

        fun sendPush(token: String, title: String, body: String ) {

            val fileName = Hibernate.getClass(this).classLoader.getResource("ThrowItOff.p12").file
            val file = File(fileName)

            val service = APNS.newService().withCert(file.absolutePath, " ").withSandboxDestination().build()
            val payload = APNS.newPayload().alertTitle(title).alertBody(body).  build()

           service.push(token, payload)
        }
    }
}