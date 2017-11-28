package br.com.testkotlinboot.pocKotlinBoot.utils

import java.util.*

class CardUtilClass {

    companion object {

        fun getCode(): String {
            val sb = StringBuilder()
            for (i in 0 until 4)
                sb.append(Random().nextInt(9))
            return sb.toString()
        }

        fun maskNumber(unmasked: String): String {
            val re = Regex("\\D")
            var r = re.replace(unmasked, "")
            return if (r.length == 16){
                "**** **** **** " + r.substring(12)
            } else r
        }
    }
}