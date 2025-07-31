import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    namespace = "com.example.myapplication" // Replace with your app's package name
    compileSdk = libs.findVersion("compile-sdk").get().toString().toInt()

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = libs.findVersion("min-sdk").get().toString().toInt()
        targetSdk = libs.findVersion("target-sdk").get().toString().toInt()
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
        sourceCompatibility = JavaVersion.toVersion(libs.findVersion("java-version").get().toString())
        targetCompatibility = JavaVersion.toVersion(libs.findVersion("java-version").get().toString())
    }

    kotlinOptions {
        jvmTarget = libs.findVersion("java-version").get().toString()
    }
}
