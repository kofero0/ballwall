plugins {
    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.jetbrainsKotlinJvm)
    id ("application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
mainClass.set("mdrew.wallball.strings.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jetbrains.kotlinx.serialization.json)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}