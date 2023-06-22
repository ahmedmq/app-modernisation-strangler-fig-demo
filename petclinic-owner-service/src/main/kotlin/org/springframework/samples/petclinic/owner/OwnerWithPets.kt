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
        var owner: Owner,
        var pets: MutableSet<Pet> = HashSet()
)

data class Owner(
        @Field("id")
        var id: Long=0L,
        @Field(name = "first_name")
        var firstName: String = "",
        @Field(name = "last_name")
        var lastName: String = "",
        var city: String = "",
        var address: String = "",
        var telephone: String = ""
)

data class Pet(
        @Field("id")
        var id: Long=0L,
        var name: String = "",
        @Field(name = "birth_date")
        var birthDate: String = "",
        @Field(name = "type_id")
        var type: Int,
        @Field(name = "owner_id")
        var ownerId: Int
)