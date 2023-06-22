package org.springframework.samples.petclinic.owner

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.lang.RuntimeException
import java.util.*

@Controller
class OwnerWithPetsController(val ownerWithPetsRepository: OwnerWithPetsRepository) {

    val VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm"

    @GetMapping("/owners/search")
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

    @GetMapping("/owners/new")
    fun initCreationForm(model: MutableMap<String, Any>): String {
        val ownerWithPets = OwnerWithPets(owner = Owner())
        model["owner"] = ownerWithPets.owner
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/owners/new")
    fun processCreationForm(@Valid owner: Owner, result: BindingResult): String {
        return if (result.hasErrors()) {
            VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            ownerWithPetsRepository.save(OwnerWithPets(owner = owner));
            "redirect:/owners/" + owner.id
        }
    }

    @GetMapping("/owners/{ownerId}/edit")
    fun initUpdateOwnerForm(@PathVariable("ownerId") ownerId: Long, model: Model): String {
        val ownerWithPets = ownerWithPetsRepository.findByOwnerId(ownerId).get()
        model.addAttribute("owner", ownerWithPets.owner)
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/owners/{ownerId}/edit")
    fun processUpdateOwnerForm(@Valid owner: Owner, result: BindingResult, @PathVariable("ownerId") ownerId: Long): String {
        return if (result.hasErrors()) {
            VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            val ownerWithPets = ownerWithPetsRepository.findByOwnerId(ownerId).get()
            ownerWithPets.owner = owner
            ownerWithPets.owner.id = ownerId
            this.ownerWithPetsRepository.save(ownerWithPets)
            "redirect:/owners/{ownerId}"
        }
    }

}