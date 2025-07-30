plugins {
    id("dev.aurakai.auraframefx.buildlogic.convention.android-library")
    id("dev.aurakai.auraframefx.buildlogic.convention.hilt")
}

android {
    namespace = "dev.aurakai.auraframefx.core"
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // Core library desugaring
    coreLibraryDesugaring(libs.core.library.desugaring)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
