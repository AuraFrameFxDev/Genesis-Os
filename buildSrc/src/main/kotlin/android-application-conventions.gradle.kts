import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // id("com.google.devtools.ksp") // Uncomment if KSP is required
}

android {
    namespace = "com.example.myapplication" // Replace with your app's package name
    compileSdk = 36 // Use your desired compileSdk version

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 33 // Use your desired minSdk version
        targetSdk = 36 // Use your desired targetSdk version
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
}
