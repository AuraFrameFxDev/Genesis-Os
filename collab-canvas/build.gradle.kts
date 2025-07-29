plugins {
    id("org.jetbrains.compose") version "1.8.2"
    id("com.android.application")
    // Add other plugins as needed
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

android {
    // Enable ViewBinding for legacy views if needed
    buildFeatures {
        viewBinding = true
        // dataBinding = true // Uncomment if needed
    }

    // Configure CMake for native code
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.29.2" // Latest stable CMake version
        }
    }

    ndkVersion = "26.2.11394342" // Latest stable NDK version
}

// Add your Compose-specific or other dependencies below
dependencies {
    implementation(compose.ui)
    implementation(compose.material)
    implementation(compose.preview)
    // Add other dependencies as needed
}