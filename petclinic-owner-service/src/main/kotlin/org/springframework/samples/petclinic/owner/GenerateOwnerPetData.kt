package org.springframework.samples.petclinic.owner

import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import org.bson.Document


object GenerateOwnerPetData {
    @JvmStatic
    fun main(args: Array<String>) {
        val uri = "mongodb://localhost:27017"

        val franklin = """
                        {
                            "id": 999,
                            "first_name": "Betty",
                            "last_name": "Davis",
                            "address": "638 Cardinal Ave.",
                            "city": "Sun Prairie",
                            "telephone": "6085551749"
                        }
                    """
        val franklinPets = listOf(
                Document.parse(
                        """
                           {
                           	"id": 999,
                           	"name": "Leo",
                           	"birth_date": "11207",
                           	"type_id": 1,
                           	"owner_id": 1
                           }
                          
                       """))

        val jeff = """
                        {
                            "id": 7,
                            "first_name": "Jeff",
                            "last_name": "Black",
                            "address": "1450 Oak Blvd.",
                            "city": "Monona",
                            "telephone": "6085555387"
                        }
                    """
        val jeffPets = listOf(
                Document.parse(
                        """
                           {
                                "id": 9,
                                "name": "Lucky",
                                "birth_date": "10809",
                                "type_id": 5,
                                "owner_id": 7
                           }
                          
                       """))

        MongoClients.create(uri).use { mongoClient ->
            val database = mongoClient.getDatabase("owners-db")
            val collection = database.getCollection("owner-with-pets")
            val ownerPetList = listOf(
                    Document().append("owner", Document.parse(franklin)).append("pets", franklinPets),
                    Document().append("owner", Document.parse(jeff)).append("pets", jeffPets)
            )
            try {
                val result = collection.insertMany(ownerPetList)
                println("Inserted document ids: " + result.insertedIds)
            } catch (me: MongoException) {
                System.err.println("Unable to insert due to an error: $me")
            }
        }
    }
}