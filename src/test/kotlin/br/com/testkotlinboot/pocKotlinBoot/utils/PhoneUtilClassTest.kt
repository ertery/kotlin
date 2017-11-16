package br.com.testkotlinboot.pocKotlinBoot.utils


import org.junit.*
import org.junit.Assert.*

class PhoneUtilClassTest{

    @Test
    fun formatPhoneTest(){
        assertEquals("1239819825", PhoneUtilClass.format("+7(123)981-98-25"))
        assertEquals("1239819825", PhoneUtilClass.format(" +7 (123) 981-98-25"))
        assertEquals("9269870046", PhoneUtilClass.format("+79269870046"))
    }

}