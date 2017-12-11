package br.com.testkotlinboot.pocKotlinBoot.controller


import br.com.testkotlinboot.pocKotlinBoot.utils.CardUtilClass
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.concurrent.schedule


@RestController
@RequestMapping("")
class PushMessageController {

    @GetMapping("/push")
    fun makePush(@RequestParam(name = "token", required = false, defaultValue = "7ea47831af8135d7d415fec3bac3d7999e18b11cfa96a977ce48c8b276bef5ef") token: String?,
                 @RequestParam(name = "delay", required = false) delay: String?) {


        if (delay != null) {
            val timer = Timer("schedule", false)
            timer.schedule(delay.toLong(), { CardUtilClass.sendPush(token!!, "Test Message", "Test body message") })
        } else CardUtilClass.sendPush(token!!, "Test Message", "Test body message")
    }

}