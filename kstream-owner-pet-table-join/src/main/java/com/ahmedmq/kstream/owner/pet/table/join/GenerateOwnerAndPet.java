package com.ahmedmq.kstream.owner.pet.table.join;

import com.ahmedmq.kstream.owner.pet.table.join.model.DefaultId;
import com.ahmedmq.kstream.owner.pet.table.join.model.Owner;
import com.ahmedmq.kstream.owner.pet.table.join.model.Pet;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class GenerateOwnerAndPet {

    public static final String PETS_TOPIC = "mysql.petclinic.pets";
    public static final String OWNERS_TOPIC = "mysql.petclinic.owners";

    public void insertData() {

        Serde<Owner> ownerSerde = new JsonSerde<>(Owner.class);
        Serde<Pet> petSerde = new JsonSerde<>(Pet.class);
        Serde<DefaultId> defaultIdSerde = new JsonSerde<>(DefaultId.class);

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, defaultIdSerde.serializer().getClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ownerSerde.serializer().getClass());

        List<KeyValue<DefaultId, Owner>> owners = List.of(
                new KeyValue<>(new DefaultId(100),
                        new Owner(100,
                                "George",
                                "Franklin",
                                "110 W. Liberty St.",
                                "Madison",
                                "6085551023")),
                new KeyValue<>(new DefaultId(200),
                        new Owner(200,
                                "Betty",
                                "Davis",
                                "638 Cardinal Ave.",
                                "Sun Prairie",
                                "6085551749")),
                new KeyValue<>(new DefaultId(300),
                        new Owner(300,
                                "Eduardo",
                                "Rodriquez",
                                "2693 Commerce St.",
                                "McFarland",
                                "6085558763"))

        );

        DefaultKafkaProducerFactory<DefaultId, Owner> ownerFactory =
                new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<DefaultId, Owner> template = new KafkaTemplate<>(ownerFactory, true);
        template.setDefaultTopic(OWNERS_TOPIC);

        for (KeyValue<DefaultId,Owner> keyValue : owners) {
            template.sendDefault(keyValue.key, keyValue.value);
        }

        List<KeyValue<DefaultId, Pet>> pets = List.of(
                new KeyValue<>(new DefaultId(11),
                        new Pet(11,
                                "Leo",
                                "2000-09-07",
                                1,
                                100)),
                new KeyValue<>(new DefaultId(12),
                        new Pet(12,
                                "Basil",
                                "2002-08-06",
                                6,
                                200)),
                new KeyValue<>(new DefaultId(13),
                        new Pet(13,
                                "Rosy",
                                "2001-04-17",
                                2,
                                300)),
                new KeyValue<>(new DefaultId(14),
                        new Pet(14,
                                "Jewel",
                                "2000-03-07",
                                5,
                                300))

        );

        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, petSerde.serializer().getClass());

        DefaultKafkaProducerFactory<DefaultId, Pet> pf1 = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<DefaultId, Pet> template1 = new KafkaTemplate<>(pf1, true);
        template1.setDefaultTopic(PETS_TOPIC);

        for (KeyValue<DefaultId, Pet> keyValue : pets) {
            template1.sendDefault(keyValue.key, keyValue.value);
        }


    }
}
