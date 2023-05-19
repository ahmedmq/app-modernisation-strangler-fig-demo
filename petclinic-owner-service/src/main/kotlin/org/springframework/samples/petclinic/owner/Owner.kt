package org.springframework.samples.petclinic.owner

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("owners")
data class Owner(
        @Id
        val id: ObjectId = ObjectId(),
        val ownerId: Int=0,
        val firstName: String = "",
        val lastName: String = "",
        val city: String = "",
        val address: String = "",
        val telephone: String = "",
        val pets: MutableSet<Pet> = HashSet()
)

data class Pet(
        val name: String = "",
        val birthDate: LocalDate? = null,
        val type: String =""
)