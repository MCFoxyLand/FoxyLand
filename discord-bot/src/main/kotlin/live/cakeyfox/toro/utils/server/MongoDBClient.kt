package live.cakeyfox.toro.utils.server

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import live.cakeyfox.toro.InoueInstance
import org.bson.Document

class MongoDBClient(
    val instance: InoueInstance
) {
    lateinit var mongoClient: MongoClient

    fun init() {
        mongoClient = MongoClients.create(instance.config.getMongoUrl())
        println("[MONGO] Connected to MongoDB!")
    }

    fun linkMinecraftAccount(userId: String, minecraftUsername: String): Boolean? {
        val collection = mongoClient.getDatabase("FoxyDatabase").getCollection("MinecraftAccounts")

        val query = Document("discordId", userId)
        val existingUser = collection.find(query).first()

        if (existingUser != null) {
            return null
        }

        val document = Document()
            .append("discordId", userId)
            .append("username", minecraftUsername)

        collection.insertOne(document)

        return true
    }
}
