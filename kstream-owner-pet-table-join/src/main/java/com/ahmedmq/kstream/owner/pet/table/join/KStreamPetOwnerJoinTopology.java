package com.ahmedmq.kstream.owner.pet.table.join;

import com.ahmedmq.kstream.owner.pet.table.join.model.DefaultId;
import com.ahmedmq.kstream.owner.pet.table.join.model.Owner;
import com.ahmedmq.kstream.owner.pet.table.join.model.OwnerWithPets;
import com.ahmedmq.kstream.owner.pet.table.join.model.Pet;
import com.ahmedmq.kstream.owner.pet.table.join.model.PetAndOwner;
import com.ahmedmq.kstream.owner.pet.table.join.serde.DefaultIdSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component("KStreamPetOwnerJoinTopology")
public class KStreamPetOwnerJoinTopology implements BiFunction<KTable<DefaultId, Owner>, KTable<DefaultId, Pet>, KTable<Integer, OwnerWithPets>> {

    public static final Serde<PetAndOwner> petAnOwnerSerde;
    public static final Serde<OwnerWithPets> ownerWithPetsSerde;

    static {
        JsonSerializer<PetAndOwner> petAndOwnerJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<PetAndOwner> petAndOwnerJsonDeserializer = new JsonDeserializer<>(PetAndOwner.class);
        petAnOwnerSerde = Serdes.serdeFrom(petAndOwnerJsonSerializer, petAndOwnerJsonDeserializer);

        JsonSerializer<OwnerWithPets> ownerWithPetsJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<OwnerWithPets> ownerWithPetsJsonDeserializer = new JsonDeserializer<>(OwnerWithPets.class);
        ownerWithPetsSerde = Serdes.serdeFrom(ownerWithPetsJsonSerializer, ownerWithPetsJsonDeserializer);

    }


    @Override
    public KTable<Integer, OwnerWithPets> apply(KTable<DefaultId, Owner> ownerKTable, KTable<DefaultId, Pet> petKTable) {

        return
                petKTable.join(ownerKTable,
                                pet -> new DefaultId(pet.owner_id()),
                                PetAndOwner::new,
                                Materialized.with(new DefaultIdSerde(), petAnOwnerSerde)
                        )
                        .groupBy(
                                (petId, petAndOwner) -> KeyValue.pair(petAndOwner.owner().id(), petAndOwner),
                                Grouped.with(Serdes.Integer(), petAnOwnerSerde)
                        )
                        .aggregate(
                                OwnerWithPets::new,
                                (ownerId, petAndOwner, agg) -> agg.addPet(petAndOwner),
                                (ownerId, petAndOwner, agg) -> agg.removePet(petAndOwner),
                                Materialized.with(Serdes.Integer(), ownerWithPetsSerde)
                        );
    }
}
