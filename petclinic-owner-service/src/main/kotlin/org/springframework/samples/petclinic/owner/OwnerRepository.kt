package org.springframework.samples.petclinic.owner

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface OwnerRepository : MongoRepository<Owner, String> {
    fun findByLastName(lastName: String): Collection<Owner>
    fun findByOwnerId(ownerId: Int): Optional<Owner>
}