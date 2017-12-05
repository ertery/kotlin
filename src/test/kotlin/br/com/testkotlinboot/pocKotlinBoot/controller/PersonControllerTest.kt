package br.com.testkotlinboot.pocKotlinBoot.controller


import br.com.testkotlinboot.pocKotlinBoot.dto.CardDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.PersonDTO
import br.com.testkotlinboot.pocKotlinBoot.dto.PurposeRecord
import br.com.testkotlinboot.pocKotlinBoot.entity.Person
import br.com.testkotlinboot.pocKotlinBoot.repository.PersonRepository
import br.com.testkotlinboot.pocKotlinBoot.repository.PurposeRepository
import br.com.testkotlinboot.pocKotlinBoot.service.PersonControllerService
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@RunWith(SpringRunner::class)
internal class PersonControllerTest {


    @Autowired
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var purposeRepository: PurposeRepository

    @Autowired
    lateinit var service: PersonControllerService


    @Before
    fun setUp() {
     //   personRepository.saveAndFlush(Person(name = "Test Test", phoneNumber = "8005553535", facebookId = "1", email = "google@goolge.com", registrationDate = LocalDateTime.now()))

    }

    @Test
    fun getPurposeByPersonId() {
    }

    @Test
    fun getPersonByFacebookId() {
        val person = service.findPersonByFacebookId("1")
        assertNotNull(person)
    }

    @Test
    fun updateState() {
    }

    @Test
    fun addState() {
    }

    @Test
    fun savePerson() {
        service.createPerson(PersonDTO(name = "Created Person",
                imagePath = "http://noimage.com",
                phoneNumber = "3345678",
                facebookId = "3",
                email = "tiffoziman@yandex.ru",
                id = 0
        ))
        val savedPerson: PersonDTO = service.findPersonByFacebookId("3") as PersonDTO
        assertNotNull(savedPerson)
        assertNull(savedPerson.paymentCard)
        assertEquals("3345678", savedPerson.phoneNumber)
    }

    @Test
    fun savePersonWithExistedNumber() {
        service.createPerson(PersonDTO(name = "Created Person",
                imagePath = "http://noimage.com",
                phoneNumber = "88005553535",
                facebookId = "2",
                email = "tiffoziman@yandex.ru",
                id = 0
        ))
        assertEquals(1, personRepository.findAll().size)
        val savedPerson = service.findPersonByFacebookId("2")
        assertEquals(-1, savedPerson)
    }

    @Test
    fun saveCardForPerson() {
       val id = service.createPerson(PersonDTO(name = "Created Person",
                imagePath = "http://noimage.com",
                phoneNumber = "33456798",
                facebookId = "4",
                email = "tiffoziman@yandex.ru",
                id = 0
        ))
        val cardId = service.addCard(id, CardDTO("3333333333333333", cardholderName = "Created Person", term = "2032-15-10", id = 0))
        val savedPerson: PersonDTO = service.findPersonByFacebookId("4") as PersonDTO
        assertNotNull(savedPerson.paymentCard)
        assertEquals(cardId, (savedPerson.paymentCard as CardDTO).id)
    }

    @Test
    fun updateCardForPerson() {
    }

    @Test
    fun getPersonService() {
    }

    @After
    fun destroy(){
        personRepository.deleteAll()
    }


}