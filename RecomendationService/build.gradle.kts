plugins {
    kotlin("jvm") version "2.2.20"
    id("io.ktor.plugin") version "3.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("io.ktor:ktor-server-status-pages:3.1.0")
    implementation("io.ktor:ktor-server-netty:3.1.0")
    implementation("io.ktor:ktor-server-core:3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.1.0")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.1.0")
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-cio:3.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.0")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
    implementation("com.rabbitmq:amqp-client:5.15.0")
    // Клиентские зависимости

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}