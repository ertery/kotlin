package br.com.testkotlinboot.pocKotlinBoot.utils

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
internal class CardUtilClassTest {

    @Test
    fun sendCodeByPush() {
       CardUtilClass.sendPush("7ea47831af8135d7d415fec3bac3d7999e18b11cfa96a977ce48c8b276bef5ef", "Тестовая отправка сообщения", "Я тебе отправил код 1111" )
    }
}