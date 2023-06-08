package com.ahmedmq.kstream.owner.pet.table.join.serde;

import com.ahmedmq.kstream.owner.pet.table.join.model.Pet;
import org.springframework.kafka.support.serializer.JsonSerde;

public class PetJsonSerde extends JsonSerde<Pet> {
}
