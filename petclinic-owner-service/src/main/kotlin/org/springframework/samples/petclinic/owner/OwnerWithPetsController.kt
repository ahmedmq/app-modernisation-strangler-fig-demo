package org.springframework.samples.petclinic.owner

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.lang.RuntimeException
import java.util.*

@Controller
class OwnerWithPetsController(val ownerWithPetsRepository: OwnerWithPetsRepository) {

    @GetMapping("/owners/find")
    fun initFindForm(model: MutableMap<String, Any>): String {
        model["owner"] = Owner()
        return "owners/findOwners"
    }

    @GetMapping("/owners")
    fun processFindForm(owner: Owner, result: BindingResult, model: MutableMap<String, Any>): String {
        val results = if (owner.lastName.isEmpty()){
            ownerWithPetsRepository.findAll()
        }else {
            ownerWithPetsRepository.findByOwnerLastName(owner.lastName)
        }
        return when {
            results.isEmpty() -> {
                // no owners found
                result.rejectValue("lastName", "notFound", "not found")
                "owners/findOwners"
            }
            results.size == 1 -> {
                // 1 owner found
                "redirect:/owners/" + results.first().owner.id
            }
            else -> {
                // multiple owners found
                model["selections"] = results
                "owners/ownersList"
            }
        }
    }

    @GetMapping("/owners/{ownerId}")
    fun showOwner(@PathVariable("ownerId") ownerId: Long, model: Model): String {
        val ownerWithPets = this.ownerWithPetsRepository.findByOwnerId(ownerId).orElseThrow { RuntimeException("Owner $ownerId not found") }
        model.addAttribute("ownerWithPets",ownerWithPets)
        return "owners/ownerDetails"
    }

}