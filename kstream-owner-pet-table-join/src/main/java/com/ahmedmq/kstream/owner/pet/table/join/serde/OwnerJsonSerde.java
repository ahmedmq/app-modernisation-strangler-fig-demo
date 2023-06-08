package com.ahmedmq.kstream.owner.pet.table.join.serde;

import com.ahmedmq.kstream.owner.pet.table.join.model.Owner;
import org.springframework.kafka.support.serializer.JsonSerde;

public class OwnerJsonSerde extends JsonSerde<Owner> {
}
