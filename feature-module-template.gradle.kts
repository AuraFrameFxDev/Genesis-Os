import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// Common configuration for feature modules
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

// Configure Android options
configure<LibraryExtension> {
    namespace = "com.genesisos.feature.${project.name.replace("-", "").lowercase()}"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

// Configure Kotlin options
configure<KotlinAndroidProjectExtension> {
    jvmToolchain(17)
}

// Dependencies
dependencies {
    // Core AndroidX
    implementation(libs.bundles.androidx.core)
    implementation(libs.bundles.compose.essentials)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Feature modules
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}

// KAPT options
kapt {
    correctErrorTypes = true
    useBuildCache = true
}
