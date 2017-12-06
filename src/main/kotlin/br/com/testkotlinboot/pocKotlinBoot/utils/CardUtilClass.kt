package br.com.testkotlinboot.pocKotlinBoot.utils

import java.util.*
import kotlin.collections.HashMap

class CardUtilClass {

    companion object {

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
    }
}