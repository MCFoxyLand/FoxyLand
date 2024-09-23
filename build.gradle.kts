plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "1.8.0"
}

group = "live.cakeyfox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://jitpack.io")
    }

    dependencies {
        // Kotlin
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")

        // Bukkit Wrapper
        implementation("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")

        // Exposed and DB
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.exposed:exposed-core:0.41.1")
        implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
        implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
        implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
        implementation("org.postgresql:postgresql:42.5.0")
        implementation("org.mongodb:mongodb-driver-sync:5.1.4")

        // Ktor
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.ktor:ktor-server-netty:2.3.12")
        implementation("io.ktor:ktor-server-core:2.3.12")
        implementation("io.ktor:ktor-server-websockets:2.3.12")
        implementation("io.ktor:ktor-server-content-negotiation:2.3.12")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
        implementation("io.ktor:ktor-client-core:2.3.12")
        implementation("io.ktor:ktor-client-cio:2.3.12")

        // VaultAPI
        compileOnly("com.github.MilkBowl:VaultAPI:1.7")

        // HikariCP
        implementation("com.zaxxer:HikariCP:4.0.3")
    }
}

kotlin {
    jvmToolchain(17)
}