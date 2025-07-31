import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Apply KSP here to make it available to all application modules
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.myapplication" // Replace with your app's package name
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
