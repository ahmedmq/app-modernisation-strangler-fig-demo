package com.ahmedmq.kstream.owner.pet.table.join;

import com.ahmedmq.kstream.owner.pet.table.join.model.DefaultId;
import com.ahmedmq.kstream.owner.pet.table.join.model.Owner;
import com.ahmedmq.kstream.owner.pet.table.join.model.OwnerWithPets;
import com.ahmedmq.kstream.owner.pet.table.join.model.Pet;
import com.ahmedmq.kstream.owner.pet.table.join.serde.DefaultIdSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component("KStreamPetOwnerJoinTopology")
public class KStreamPetOwnerJoinTopology implements BiFunction<KTable<DefaultId, Owner>, KStream<DefaultId, Pet>, KTable<DefaultId, OwnerWithPets>> {

    public static final Serde<OwnerWithPets> ownerWithPetsSerde;

    public DefaultIdSerde defaultIdSerde = new DefaultIdSerde();

    static {
        JsonSerializer<OwnerWithPets> ownerWithPetsJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<OwnerWithPets> ownerWithPetsJsonDeserializer = new JsonDeserializer<>(OwnerWithPets.class);
        ownerWithPetsSerde = Serdes.serdeFrom(ownerWithPetsJsonSerializer, ownerWithPetsJsonDeserializer);
    }


    @Override
    public KTable<DefaultId, OwnerWithPets> apply(KTable<DefaultId, Owner> ownerKTable, KStream<DefaultId, Pet> petKStream) {

        KTable<DefaultId, OwnerWithPets> petByOwnerIdKTable = petKStream.selectKey((k, v) -> new DefaultId(v.owner_id()))
                .groupByKey()
                .aggregate(
                        OwnerWithPets::new,
                        (key, value, aggregate) -> {
                            aggregate.addPet(null, value);
                            return aggregate;
                        },
                        Materialized.<DefaultId, OwnerWithPets, KeyValueStore<Bytes, byte[]>>as("pet-by-owner-id-store")
                                .withKeySerde(defaultIdSerde)
                                .withValueSerde(ownerWithPetsSerde));

        return
                ownerKTable.leftJoin(petByOwnerIdKTable, (owner, pets) -> {
                    pets = pets == null ? new OwnerWithPets() : pets;
                    pets.setOwner(owner);
                    return pets;
                }, Materialized.<DefaultId, OwnerWithPets, KeyValueStore<Bytes, byte[]>>as("owner-with-pets-store")
                        .withKeySerde(defaultIdSerde)
                        .withValueSerde(ownerWithPetsSerde));
    }
}
