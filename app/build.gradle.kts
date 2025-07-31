plugins {
    id("dev.aurakai.auraframefx.buildlogic.convention.android-application")
    id("dev.aurakai.auraframefx.buildlogic.convention.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "dev.aurakai.auraframefx.genesis"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx.genesis"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    
    // Network
    implementation(libs.bundles.networking)

    // UI
    implementation(libs.coil.compose)
    implementation(libs.timber)
    
    // Project dependencies
    implementation(projects.sandboxUi)
    implementation(projects.collabCanvas)
    implementation(projects.oracledrive)

    // Core Library Desugaring
    coreLibraryDesugaring(libs.core.library.desugaring)
    
    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.compose.testing)

    // Debug
    debugImplementation(libs.bundles.compose.debug)
}

// === OpenAPI Configuration ===
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/api-spec/openapi.yml")
    outputDir.set(layout.buildDirectory.dir("generated/openapi"))

    configOptions.set(mapOf(
        "useCoroutines" to "true",
        "serializationLibrary" to "kotlinx_serialization"
    ))
}