plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "dev.aurakai.auraframefx.securecomm"
    compileSdk = 34

    defaultConfig {
        minSdk = 33
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    
    // Configure targetSdk for test and lint
    testOptions {
        targetSdk = 34
    }
    
    lint {
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    // Configure Java toolchain
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }
    
    // Configure Kotlin compiler options
    kotlin {
        jvmToolchain(24)
        
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
            freeCompilerArgs.addAll(
                "-Xuse-k2",
                "-Xskip-prerelease-check",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.ExperimentalStdlibApi",
                "-opt-in=kotlin.contracts.ExperimentalContracts",
                "-Xjvm-default=all"
            )
        }
    }
    
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.2.0-beta01"
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")
    
    // AndroidX
    implementation("androidx.core:core-ktx:1.16.0")
    
    // Security
    implementation("androidx.security:security-crypto:1.1.0-beta01")
    implementation("com.google.crypto.tink:tink-android:1.18.0")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.57")
    ksp("com.google.dagger:hilt-compiler:2.57")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    
    // Bouncy Castle for cryptographic operations
    implementation("org.bouncycastle:bcprov-jdk18on:1.81")
}
