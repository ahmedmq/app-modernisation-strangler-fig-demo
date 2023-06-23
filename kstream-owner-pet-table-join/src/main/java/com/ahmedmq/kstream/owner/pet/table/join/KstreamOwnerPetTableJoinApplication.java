package com.ahmedmq.kstream.owner.pet.table.join;

import com.ahmedmq.kstream.owner.pet.table.join.model.Owner;
import com.ahmedmq.kstream.owner.pet.table.join.model.OwnerWithPets;
import com.ahmedmq.kstream.owner.pet.table.join.model.Pet;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class KstreamOwnerPetTableJoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(KstreamOwnerPetTableJoinApplication.class, args);
    }


    @Bean("KStreamUnwrapPetOwnerTopology")
    public Function<KStream<String, OwnerWithPets>, KStream<Integer, Object>[]> kStreamUnwrapPetOwnerTopology(){
        return ownerWithPetsKStream -> {
            // Split the stream into two streams of OwnersWithPets, one contains only owners and the other contains only list of pets
            Map<String, KStream<String, OwnerWithPets>> stringKStreamMap = ownerWithPetsKStream.flatMapValues(value ->
                            Arrays.asList(new OwnerWithPets(value.owner, null),
                                    new OwnerWithPets(null, value.pets))
                    ).split(Named.as("petclinic-"))
                    .branch((key, value) -> value.pets == null, Branched.as("owners"))
                    .branch((key, value) -> value.owner == null, Branched.as("pets"))
                    .noDefaultBranch();

            // From the first branch, generate a stream of owners
            KStream<Integer, Owner> ownerKStream = stringKStreamMap
                    .get("petclinic-owners")
                    .map((key, value) -> new KeyValue<>(value.owner.id(), value.owner));

            // From the second branch, generate a stream of pets by flattening the list of pets
            KStream<Integer,Pet> petKStream = stringKStreamMap
                    .get("petclinic-pets")
                    .flatMapValues(value -> new ArrayList<>(value.pets))
                    .map((key, value) -> new KeyValue<>(value.id(), value));

            // return the two streams
            return new KStream[]{ownerKStream, petKStream};
        };
    }

}
