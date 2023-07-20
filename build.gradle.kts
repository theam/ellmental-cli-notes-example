import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.theagilemonkeys"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.theam.ellmental:core:main-SNAPSHOT")
    implementation("com.github.theam.ellmental:semanticsearch:main-SNAPSHOT")
    implementation("com.github.theam.ellmental:vectorstore:main-SNAPSHOT")
    implementation("com.github.theam.ellmental:embeddingsmodel:main-SNAPSHOT")
    implementation("com.aallam.openai:openai-client:3.3.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

application {
    mainClass.set("MainKt")
}