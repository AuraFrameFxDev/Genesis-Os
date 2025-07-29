plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

// Your android { ... } block here

dependencies {
    // Use aliases for libraries if you have them in your TOML
    // For example:
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.preview)
}
