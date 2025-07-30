plugins {
    id("dev.aurakai.auraframefx.buildlogic.convention.android-application")
}

android {
    namespace = "dev.aurakai.auraframefx"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        versionCode = 1
        versionName = "1.0"
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = libs.versions.cmake.get()
        }
    }

    ndkVersion = libs.versions.ndk.get()
}

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.preview)
    // Add other dependencies/aliases as needed
}