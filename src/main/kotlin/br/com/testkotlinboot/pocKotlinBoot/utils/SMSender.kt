package br.com.testkotlinboot.pocKotlinBoot.utils

import org.springframework.stereotype.Service
import java.io.*
import java.lang.Thread.sleep
import java.net.URL
import java.net.URLEncoder

@Service
class SMSender(private final val values: ServiceValues) {

    private val SMSC_LOGIN = values.smsLogin    // логин клиента
    private val SMSC_PASSWORD = values.smsPass // пароль
    private val SMSC_HTTPS = true         // использовать HTTPS протокол
    private val SMSC_CHARSET = "utf-8"       // кодировка сообщения: koi8-r, windows-1251 или utf-8 (по умолчанию)
    private val SMSC_DEBUG = true         // флаг отладки
    private val SMSC_POST = false         // Использовать метод POST


    fun sendSms(phones: String, message: String, fake: Boolean): Array<String> {
        return if (fake) {
            sleep(4000)
            arrayOf("FAKE", "1")
        }
        else sendSms(phones, message, 0, "", "", 0, "", "")
    }

    /**
     * Отправка SMS
     *
     * @param phones   - список телефонов через запятую или точку с запятой
     * @param message  - отправляемое сообщение
     * @param translit - переводить или нет в транслит (1,2 или 0)
     * @param time     - необходимое время доставки в виде строки (DDMMYYhhmm, h1-h2, 0ts, +m)
     * @param id       - идентификатор сообщения. Представляет собой 32-битное число в диапазоне от 1 до 2147483647.
     * @param format   - формат сообщения (0 - обычное sms, 1 - flash-sms, 2 - wap-push, 3 - hlr, 4 - bin, 5 - bin-hex, 6 - ping-sms, 7 - mms, 8 - mail, 9 - call)
     * @param sender   - имя отправителя (Sender ID). Для отключения Sender ID по умолчанию необходимо в качестве имени передать пустую строку или точку.
     * @param query    - строка дополнительных параметров, добавляемая в URL-запрос ("valid=01:00&maxsms=3&tz=2")
     * @return array (<id>, <количество sms>, <стоимость>, <баланс>) в случае успешной отправки
     * или массив (<id>, -<код ошибки>) в случае ошибки
    </код></id></баланс></стоимость></количество></id> */

    private fun sendSms(phones: String, message: String, translit: Int, time: String, id: String, format: Int, sender: String, query: String): Array<String> {
        val formats = arrayOf("", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1")

        val messages: Array<String> = smscSendCmd("send", "cost=3&phones=" + URLEncoder.encode(phones, SMSC_CHARSET)
                + "&mes=" + URLEncoder.encode(message, SMSC_CHARSET)
                + "&translit=" + translit + "&id=" + id + (if (format > 0) "&" + formats[format] else "")
                + (if (sender === "") "" else "&sender=" + URLEncoder.encode(sender, SMSC_CHARSET))
                + (if (time === "") "" else "&time=" + URLEncoder.encode(time, SMSC_CHARSET))
                + if (query === "") "" else "&$query")

        if (messages.size > 1) {
            if (SMSC_DEBUG) {
                if (Integer.parseInt(messages[1]) > 0) {
                    println("Сообщение отправлено успешно. ID: " + messages[0] + ", всего SMS: " + messages[1] + ", стоимость: " + messages[2] + ", баланс: " + messages[3])
                } else {
                    print("Ошибка №" + Math.abs(Integer.parseInt(messages[1])))
                    println(if (Integer.parseInt(messages[0]) > 0) ", ID: " + messages[0] else "")
                }
            }
        } else {
            println("Не получен ответ от сервера.")
        }

        return messages
    }


    /**
     * Получение стоимости SMS
     *
     * @param phones   - список телефонов через запятую или точку с запятой
     * @param message  - отправляемое сообщение.
     * @param translit - переводить или нет в транслит (1,2 или 0)
     * @param format   - формат сообщения (0 - обычное sms, 1 - flash-sms, 2 - wap-push, 3 - hlr, 4 - bin, 5 - bin-hex, 6 - ping-sms, 7 - mms, 8 - mail, 9 - call)
     * @param sender   - имя отправителя (Sender ID)
     * @param query    - строка дополнительных параметров, добавляемая в URL-запрос ("list=79999999999:Ваш пароль: 123\n78888888888:Ваш пароль: 456")
     * @return array(<стоимость>, <количество sms>) либо (0, -<код ошибки>) в случае ошибки
    </код></количество></стоимость> */

    fun get_sms_cost(phones: String, message: String, translit: Int, format: Int, sender: String, query: String): Array<String> {
        val formats = arrayOf("", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1")
        val messages = smscSendCmd("send", "cost=1&phones=" + URLEncoder.encode(phones, SMSC_CHARSET)
                + "&mes=" + URLEncoder.encode(message, SMSC_CHARSET)
                + "&translit=" + translit + (if (format > 0) "&" + formats[format] else "")
                + (if (sender === "") "" else "&sender=" + URLEncoder.encode(sender, SMSC_CHARSET))
                + if (query === "") "" else "&$query")


        // (cost, cnt) или (0, -error)

        if (messages.size > 1) {
            if (SMSC_DEBUG) {
                if (Integer.parseInt(messages[1]) > 0)
                    println("Стоимость рассылки: " + messages[0] + ", Всего SMS: " + messages[1])
                else
                    print("Ошибка №" + Math.abs(Integer.parseInt(messages[1])))
            }
        } else
            println("Не получен ответ от сервера.")

        return messages
    }

    /**
     * Проверка статуса отправленного SMS или HLR-запроса
     *
     * @param id    - ID cообщения
     * @param phone - номер телефона
     * @param all   - дополнительно возвращаются элементы в конце массива:
     * (<время отправки>, <номер телефона>, <стоимость>, <sender id>, <название статуса>, <текст сообщения>)
     * @return array
     * для отправленного SMS (<статус>, <время изменения>, <код ошибки sms>)
     * для HLR-запроса (<статус>, <время изменения>, <код ошибки sms>, <код страны регистрации>, <код оператора абонента>,
     * <название страны регистрации>, <название оператора абонента>, <название роуминговой страны>, <название роумингового оператор></название><код IMSI SIM-карты>, <номер сервис-центра>)
     * либо array(0, -<код ошибки>) в случае ошибки
    </код></номер></код></название></название></название></код></код></код></время></статус></код></время></статус></текст></название></sender></стоимость></номер></время> */

    fun get_status(id: Int, phone: String, all: Int): Array<String> {
        var m = arrayOf<String>()
        val tmp: String

        try {
            m = smscSendCmd("status", "phone=" + URLEncoder.encode(phone, SMSC_CHARSET) + "&id=" + id + "&all=" + all)

            if (m.size > 1) {
                if (SMSC_DEBUG) {
                    if (m[1] != "" && Integer.parseInt(m[1]) >= 0) {
                        val currentTimestamp = java.sql.Timestamp(Integer.parseInt(m[1]).toLong())
                        println("Статус SMS = " + m[0])
                    } else
                        println("Ошибка №" + Math.abs(Integer.parseInt(m[1])))
                }

                if (all == 1 && m.size > 9 && (m.size < 14 || m[14] != "HLR")) {
                    tmp = implode(m)
                    m = tmp.split(",".toRegex(), 9).toTypedArray()
                }
            } else
                println("Не получен ответ от сервера.")

        } catch (ignored: UnsupportedEncodingException) {

        }

        return m
    }

    /**
     * Получениe баланса
     *
     * @return String баланс или пустую строку в случае ошибки
     */

    fun getBalance(): String {
        val m: Array<String> = smscSendCmd("balance", "")

        // (balance) или (0, -error)

        if (m.size > 1) {
            if (SMSC_DEBUG) {
                if (m.size == 1)
                    println("Сумма на счете: " + m[0])
                else
                    println("Ошибка №" + Math.abs(Integer.parseInt(m[1])))
            }
        } else {
            println("Не получен ответ от сервера.")
        }
        return if (m.size == 2) "" else m[0]
    }

    /**
     * Формирование и отправка запроса
     *
     * @param cmd - требуемая команда
     * @param arg - дополнительные параметры
     */

    private fun smscSendCmd(cmd: String, arg: String): Array<String> {
        var ret = ","

            var url = ((if (SMSC_HTTPS) "https" else "http") + "://smsc.ru/sys/" + cmd + ".php?login=" + URLEncoder.encode(SMSC_LOGIN, SMSC_CHARSET)
                    + "&psw=" + URLEncoder.encode(SMSC_PASSWORD, SMSC_CHARSET)
                    + "&fmt=1&charset=" + SMSC_CHARSET + "&" + arg)

            var i = 0
            do {
                if (i++ > 0) {
                    url = url.replace("://smsc.ru/", "://www$i.smsc.ru/")
                }
                ret = smscReadUrl(url)
            } while (ret == "" && i < 5)


        return ret.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    /**
     * Чтение URL
     *
     * @param url - ID cообщения
     * @return line - ответ сервера
     */
    private fun smscReadUrl(url: String): String {

        val line = StringBuilder()
        var real_url = url
        var param = arrayOf<String>()
        val isPost = SMSC_POST || url.length > 2000

        if (isPost) {
            param = url.split("\\?".toRegex(), 2).toTypedArray()
            real_url = param[0]
        }

            val u = URL(real_url)
            val `is`: InputStream

            if (isPost) {
                val conn = u.openConnection()
                conn.doOutput = true
                val os = OutputStreamWriter(conn.getOutputStream(), SMSC_CHARSET)
                os.write(param[1])
                os.flush()
                os.close()
                println("post")
                `is` = conn.getInputStream()
            } else {
                `is` = u.openStream()
            }

            val reader = InputStreamReader(`is`, SMSC_CHARSET)

            var ch: Int? = null
            while ({ ch = reader.read(); ch }() != -1) {
                line.append(ch!!.toChar())
            }

            reader.close()


        return line.toString()
    }

    private fun implode(ary: Array<String>): String {
        val out = StringBuilder()

        for (i in ary.indices) {
            if (i != 0)
                out.append(",")
            out.append(ary[i])
        }

        return out.toString()
    }


}