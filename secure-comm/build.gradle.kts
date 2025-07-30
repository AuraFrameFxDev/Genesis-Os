plugins {
    id("dev.aurakai.auraframefx.buildlogic.convention.android-library")
    id("dev.aurakai.auraframefx.buildlogic.convention.hilt")
}

android {
    namespace = "dev.aurakai.auraframefx.securecomm"
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    
    // AndroidX
    implementation(libs.androidx.core.ktx)
    
    // Security
    implementation(libs.androidx.security.crypto)
    implementation(libs.google.crypto.tink.android)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    
    // Bouncy Castle for cryptographic operations
    implementation(libs.bouncycastle.bcprov)
}
