package org.springframework.samples.petclinic.owner

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("owner-with-pets")
data class OwnerWithPets(
        @Id
        @Field(name = "_id")
        val id: ObjectId = ObjectId(),
        val owner: Owner,
        val pets: MutableSet<Pet> = HashSet()
)

data class Owner(
        @Field("id")
        val id: Long=0L,
        @Field(name = "first_name")
        val firstName: String = "",
        @Field(name = "last_name")
        val lastName: String = "",
        val city: String = "",
        val address: String = "",
        val telephone: String = ""
)

data class Pet(
        @Field("id")
        val id: Long=0L,
        val name: String = "",
        @Field(name = "birth_date")
        val birthDate: String = "",
        @Field(name = "type_id")
        val type: Int
)