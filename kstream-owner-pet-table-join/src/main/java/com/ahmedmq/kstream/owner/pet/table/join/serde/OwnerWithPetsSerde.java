package com.ahmedmq.kstream.owner.pet.table.join.serde;

import com.ahmedmq.kstream.owner.pet.table.join.model.OwnerWithPets;
import org.springframework.kafka.support.serializer.JsonSerde;

public class OwnerWithPetsSerde extends JsonSerde<OwnerWithPets> {

}
