plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-Xuse-k2"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/DEPENDENCIES"
            )
        }
    }
}

dependencies {
    // === BOM Platform Dependencies ===
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.firebase.bom))
    
    // === Core Android ===
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    
    // === Bundles (Clean & Organized) ===
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.room)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.datastore)
    
    // === Utilities ===
    implementation(libs.timber)
    implementation(libs.coil.compose)
    
    // === Development ===
    coreLibraryDesugaring(libs.core.library.desugaring)
    
    // === KSP Processors (No KAPT!) ===
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
    
    // === Local JARs (LSPosed/Xposed) ===
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // === Testing ===
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.compose.testing)

    // === Debug ===
    debugImplementation(libs.bundles.compose.debug)
}

// === OpenAPI Configuration ===
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/openapi.yml")
    outputDir.set(layout.buildDirectory.dir("generated/openapi"))

    configOptions.set(mapOf(
        "useCoroutines" to "true",
        "serializationLibrary" to "kotlinx_serialization"
    ))
}