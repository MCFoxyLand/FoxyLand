package live.cakeyfox.toro

import com.mongodb.client.MongoClient
import live.cakeyfox.toro.utils.ConfigManager
import live.cakeyfox.toro.utils.discord.ChatManager
import live.cakeyfox.toro.utils.discord.commands.structures.CommandHandler
import live.cakeyfox.toro.utils.discord.listener.MajorEventListener
import live.cakeyfox.toro.utils.server.LandCore
import live.cakeyfox.toro.utils.server.MongoDBClient
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

class InoueInstance {
    lateinit var jda: JDA
    lateinit var chat: ChatManager
    lateinit var config: ConfigManager
    lateinit var mongoClient: MongoDBClient
    lateinit var commandHandler: CommandHandler

    fun start() {
        config = ConfigManager()
        commandHandler = CommandHandler(this)

        mongoClient = MongoDBClient(this)
        mongoClient.init()

        jda = JDABuilder.createDefault(config.getToken()).build()
        jda.addEventListener(MajorEventListener(this))
        jda.awaitReady()

        chat = ChatManager(this)
        LandCore(this).start()
    }
}