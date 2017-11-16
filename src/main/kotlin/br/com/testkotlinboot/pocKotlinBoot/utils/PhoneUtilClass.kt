package br.com.testkotlinboot.pocKotlinBoot.utils

class PhoneUtilClass {

    companion object {
        fun format(phoneNumber: String) : String {
           val re = Regex("\\D")
           return re.replace(phoneNumber, "").removePrefix("7")
        }
    }

}