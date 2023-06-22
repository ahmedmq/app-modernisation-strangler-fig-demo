package org.springframework.samples.petclinic.owner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetClinicOwnerServiceApplication

fun main(args: Array<String>) {
    runApplication<PetClinicOwnerServiceApplication>(*args)
}



