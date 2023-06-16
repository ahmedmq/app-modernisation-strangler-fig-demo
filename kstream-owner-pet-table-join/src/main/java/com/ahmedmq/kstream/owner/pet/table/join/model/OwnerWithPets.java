package com.ahmedmq.kstream.owner.pet.table.join.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class OwnerWithPets {

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerWithPets.class);


    public Owner owner;
    public List<Pet> pets = new ArrayList<>();

    public void setOwner(Owner owner) {
        this.owner = owner;
    }


    public OwnerWithPets addPet(Owner owner, Pet pet) {
        LOGGER.info("Adding owner: {} and pet {} ", owner, pet);

        this.owner = owner;
        this.pets.add(pet);

        return this;
    }

    public OwnerWithPets removePet(Pet pet) {
        LOGGER.info("Removing: {}" , pet);

        Iterator<Pet> it = this.pets.iterator();
        while (it.hasNext()) {
            Pet p = it.next();
            if (Objects.equals(p.id(), pet.id())) {
                it.remove();
                break;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return "OwnerWithPets{" +
                "owner=" + owner +
                ", pets=" + pets +
                '}';
    }
}
