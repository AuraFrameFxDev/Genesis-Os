import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // Apply KSP here to make it available to all library modules
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.mylibrary" // Replace with your library's package name
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion("24")
        targetCompatibility = JavaVersion.toVersion("24")
    }

    kotlinOptions {
        jvmTarget = "24"
    }
}
