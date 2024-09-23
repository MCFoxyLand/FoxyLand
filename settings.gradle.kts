plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "foxland"
include("minecraft:FoxCore")
include("minecraft:FoxConomy")
include("discord-bot")