plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    
    // AndroidX
    implementation(libs.androidx.core.ktx)
    
    // Security
    implementation(libs.androidx.security.crypto)
    implementation(libs.google.crypto.tink.android)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    
    // Bouncy Castle for cryptographic operations
    implementation(libs.bouncycastle.bcprov)
}
