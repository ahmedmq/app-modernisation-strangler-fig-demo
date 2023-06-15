package org.springframework.samples.petclinic.owner

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface OwnerWithPetsRepository : MongoRepository<OwnerWithPets, String> {
    fun findByOwnerLastName(lastName: String): Collection<OwnerWithPets>
    fun findByOwnerId(ownerId: Long): Optional<OwnerWithPets>
}