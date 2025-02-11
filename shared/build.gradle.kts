import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "2.0.21"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).takeIf { "XCODE_VERSION_MAJOR" in System.getenv().keys }
        ?.forEach {
            it.binaries.framework {
                baseName = "shared"
                export(libs.decompose.decompose)
                export(libs.essenty.lifecycle)
                export(libs.essenty.stateKeeper)
                export(libs.essenty.instanceKeeper)
            }
        }

    sourceSets {
        commonMain.dependencies {
            api(libs.decompose.decompose)
            api(libs.essenty.lifecycle)
            api(libs.essenty.stateKeeper)
            api(libs.essenty.instanceKeeper)
            implementation(libs.ktor.client.core)
            implementation(libs.jetbrains.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "mdrew.ballwall.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}