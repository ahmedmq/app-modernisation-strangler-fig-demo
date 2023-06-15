package org.springframework.samples.petclinic.owner

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@SpringBootApplication
class PetClinicOwnerServiceApplication {

//	@Bean
//	@Profile("!test")
//	fun init(ownerWithPetsRepository: OwnerWithPetsRepository) : CommandLineRunner {
//		return CommandLineRunner {
//			val franklin = Owner(
//					id = 1 ,
//					firstName = "George",
//					lastName = "Franklin",
//					city = "Madison",
//					address = "110 W. Liberty St.",
//					telephone = "6085551023")
//			val franklinWithLeo = OwnerWithPets(owner = franklin ).copy(
//					pets = mutableSetOf(Pet(
//							id = 1,
//							name = "Leo",
//							birthDate = LocalDate.now().toString(),
//							type = 1
//					))
//			)
//			val davis =Owner(
//					id = 2,
//					firstName = "Betty",
//					lastName = "Davis",
//					city = "Sun Prairie",
//					address = "638 Cardinal Ave.",
//					telephone = "6085551749")
//			val davisWithBasil = OwnerWithPets(owner = davis).copy(
//					owner = Owner(
//						id = 2,
//						firstName = "Betty",
//						lastName = "Davis",
//						city = "Sun Prairie",
//						address = "638 Cardinal Ave.",
//						telephone = "6085551749"),
//					pets = mutableSetOf(Pet(
//							id = 2,
//							name = "Basil",
//							birthDate = LocalDate.now().toString(),
//							type = 1
//
//					))
//			)
//			ownerWithPetsRepository.saveAll(listOf(franklinWithLeo, davisWithBasil))
//		}
//	}
}

fun main(args: Array<String>) {
	runApplication<PetClinicOwnerServiceApplication>(*args)
}



