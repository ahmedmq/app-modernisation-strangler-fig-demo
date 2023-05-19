package org.springframework.samples.petclinic.owner

import org.assertj.core.util.Lists
import org.bson.types.ObjectId
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*

@WebMvcTest(OwnerController::class)
class OwnerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var owners: OwnerRepository

    private lateinit var george: Owner

    @BeforeEach
    fun setup() {
        george = Owner(
                id = ObjectId(),
                ownerId = 1,
                firstName = "George",
                lastName = "Franklin",
                address = "110 W. Liberty St.",
                city = "Madison",
                telephone = "6085551023",
                pets = mutableSetOf(Pet(
                        name = "Max",
                        birthDate = LocalDate.now(),
                        type = "Dog"
                ))
        )
        given(owners.findById(george.id.toHexString())).willReturn(Optional.of(george))
    }

    @Test
    fun shouldReturnOwners() {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk)
    }

    @Test
    fun testInitFindForm() {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"))
    }

    @Test
    fun testProcessFindFormSuccess() {
        given(owners.findAll()).willReturn(Lists.newArrayList<Owner>(george, Owner()))
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk)
                .andExpect(view().name("owners/ownersList"))
    }

    @Test
    fun testProcessFindFormByLastName() {
        given(owners.findByLastName(george.lastName)).willReturn(Lists.newArrayList<Owner>(george))
        mockMvc.perform(get("/owners")
                .param("lastName", "Franklin")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/owners/" + george.ownerId))
    }

    @Test
    fun testProcessFindFormNoOwnersFound() {
        mockMvc.perform(get("/owners")
                .param("lastName", "Unknown Surname")
        )
                .andExpect(status().isOk)
                .andExpect(model().attributeHasFieldErrors("owner", "lastName"))
                .andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
                .andExpect(view().name("owners/findOwners"))
    }

    @Test
    fun testShowOwner() {
        given(owners.findByOwnerId(george.ownerId)).willReturn(Optional.of(george))
        mockMvc.perform(get("/owners/{ownerId}", george.ownerId))
                .andExpect(status().isOk)
                .andExpect(model().attribute("owner", hasProperty<Any>("lastName", `is`("Franklin"))))
                .andExpect(model().attribute("owner", hasProperty<Any>("firstName", `is`("George"))))
                .andExpect(model().attribute("owner", hasProperty<Any>("address", `is`("110 W. Liberty St."))))
                .andExpect(model().attribute("owner", hasProperty<Any>("city", `is`("Madison"))))
                .andExpect(model().attribute("owner", hasProperty<Any>("telephone", `is`("6085551023"))))
                .andExpect(model().attribute("owner", hasProperty<Any>("pets", not<Any>(empty<Any>()))))
                .andExpect(view().name("owners/ownerDetails"))

    }


}