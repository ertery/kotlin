package br.com.testkotlinboot.pocKotlinBoot.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

internal class PaymentControllerServiceTest {


    @Autowired
    lateinit var service: PaymentControllerService

    @Test
    fun sendPushToIos() {
        service.sendCodeByPush(12)
    }

}