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

@WebMvcTest(OwnerWithPetsController::class)
class OwnerWithPetsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var ownerWithPetsRepository: OwnerWithPetsRepository

    private lateinit var georgeWithMax: OwnerWithPets
    private lateinit var franklinWithLeo: OwnerWithPets

    @BeforeEach
    fun setup() {
         georgeWithMax = OwnerWithPets(id = ObjectId(),
                owner = Owner(
                        id = 1,
                        firstName = "George",
                        lastName = "Franklin",
                        address = "110 W. Liberty St.",
                        city = "Madison",
                        telephone = "6085551023"
                ),
                pets = mutableSetOf(Pet(
                        id = 1,
                        name = "Max",
                        birthDate = LocalDate.now().toString(),
                        type = 1,
                        ownerId = 1
                )))
        given(ownerWithPetsRepository.findById(georgeWithMax.id.toHexString())).willReturn(Optional.of(georgeWithMax))
        franklinWithLeo = OwnerWithPets(
                id= ObjectId(),
                owner=Owner(
                        id = 1 ,
                        firstName = "George",
                        lastName = "Franklin",
                        city = "Madison",
                        address = "110 W. Liberty St.",
                        telephone = "6085551023"),
                pets = mutableSetOf(Pet(
                        id = 1,
                        name = "Leo",
                        birthDate = LocalDate.now().toString(),
                        type = 1,
                        ownerId = 1
                ))
        )
    }

    @Test
    fun shouldReturnOwners() {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk)
    }

    @Test
    fun testInitFindForm() {
        mockMvc.perform(get("/owners/search"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"))
    }

    @Test
    fun testProcessFindFormSuccess() {
        given(ownerWithPetsRepository.findAll()).willReturn(Lists.newArrayList(georgeWithMax, franklinWithLeo))
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk)
                .andExpect(view().name("owners/ownersList"))
    }

    @Test
    fun testProcessFindFormByLastName() {
        given(ownerWithPetsRepository.findByOwnerLastName(georgeWithMax.owner.lastName)).willReturn(Lists.newArrayList<OwnerWithPets>(georgeWithMax))
        mockMvc.perform(get("/owners")
                .param("lastName", "Franklin")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/owners/" + georgeWithMax.owner.id))
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
        given(ownerWithPetsRepository.findByOwnerId(georgeWithMax.owner.id)).willReturn(Optional.of(georgeWithMax))
        mockMvc.perform(get("/owners/{ownerId}", georgeWithMax.owner.id))
                .andExpect(status().isOk)
//                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("lastName", `is`("Franklin"))))
//                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("firstName", `is`("George"))))
//                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("address", `is`("110 W. Liberty St."))))
//                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("city", `is`("Madison"))))
//                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("telephone", `is`("6085551023"))))
                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("owner", not<Any>(empty<Any>()))))
                .andExpect(model().attribute("ownerWithPets", hasProperty<Any>("pets", not<Any>(empty<Any>()))))
                .andExpect(view().name("owners/ownerDetails"))

    }


}