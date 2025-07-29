repositories {
    mavenCentral()
}

android {
    // Enable ViewBinding for legacy views if needed
    buildFeatures.viewBinding = true

    // Enable data binding if needed
    // buildFeatures.dataBinding = true

    // Configure CMake for native code
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.29.2" // Latest stable CMake version
        }
    }

    ndkVersion = "26.2.11394342" // Latest stable NDK version
}
