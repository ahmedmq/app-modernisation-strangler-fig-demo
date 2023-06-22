package org.springframework.samples.petclinic.owner

import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.LocalDate

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OwnerWithPetsControllerIT {

    companion object {
        @Container
        private val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:6.0"))

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getConnectionString)
        }
    }

    @Autowired
    private lateinit var ownerWithPetsRepository: OwnerWithPetsRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldReturnOwners() {

        val georgeWithMax = OwnerWithPets(id = ObjectId(),
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

        ownerWithPetsRepository.save(georgeWithMax)

        mockMvc.perform(MockMvcRequestBuilders.get("/owners")
                .param("lastName", "Franklin")
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
                .andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + georgeWithMax.owner.id))
    }
}