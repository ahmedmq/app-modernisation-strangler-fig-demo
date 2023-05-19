package org.springframework.samples.petclinic.owner

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@SpringBootApplication
class PetClinicOwnerServiceApplication {

	@Bean
	@Profile("!test")
	fun init(ownerRepository: OwnerRepository) : CommandLineRunner {
		return CommandLineRunner {
			val franklin = Owner().copy(
					ownerId = 1 ,
					firstName = "George",
					lastName = "Franklin",
					city = "Madison",
					address = "110 W. Liberty St.",
					telephone = "6085551023",
					pets = mutableSetOf(Pet(
						name = "Leo",
						birthDate = LocalDate.now()
					))
			)
			val davis = Owner().copy(
					ownerId = 2,
					firstName = "Betty",
					lastName = "Davis",
					city = "Sun Prairie",
					address = "638 Cardinal Ave.",
					telephone = "6085551749",
					pets = mutableSetOf(Pet(
							name = "Basil",
							birthDate = LocalDate.now()
					))
			)
			ownerRepository.saveAll(listOf(franklin, davis))
		}
	}
}

fun main(args: Array<String>) {
	runApplication<PetClinicOwnerServiceApplication>(*args)
}



