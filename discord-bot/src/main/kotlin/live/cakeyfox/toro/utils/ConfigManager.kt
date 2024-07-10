package live.cakeyfox.toro.utils

import java.io.File
import java.io.IOException
import java.util.Properties

class ConfigManager {
    private val properties = Properties()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        val resourceStream = javaClass.classLoader.getResourceAsStream("config.conf")
            ?: throw IOException("Resource config.conf not found")

        try {
            resourceStream.use { inputStream ->
                properties.load(inputStream)
            }
        } catch (e: IOException) {
            System.err.println("Failed to load configuration: ${e.message}")
        }
    }

    fun getToken(): String {
        return properties.getProperty("token") ?: throw IllegalStateException("Token not found in configuration")
    }

    fun getLogChannel(): String {
        return properties.getProperty("logChannel") ?: throw IllegalStateException("Log channel not found in configuration")
    }

    fun getPSQLUser(): String {
        return properties.getProperty("database") ?: throw IllegalStateException("Database user not found in configuration")
    }

    fun getPSQLPasswd(): String {
        return properties.getProperty("password") ?: throw IllegalStateException("Password not found in configuration")
    }
}