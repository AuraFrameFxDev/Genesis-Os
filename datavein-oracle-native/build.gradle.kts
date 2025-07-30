@file:Suppress("DSL_SCOPE_VIOLATION")

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "dev.aurakai.auraframefx.oraclenative"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Enable prefab for native dependencies
        buildConfigField("boolean", "ENABLE_NATIVE_LOGGING", "true")

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++20")
                arguments(
                    "-DANDROID_STL=c++_shared",
                    "-DCMAKE_VERBOSE_MAKEFILE=ON"
                )
                abiFilters(
                    "arm64-v8a",
                    "armeabi-v7a",
                    "x86_64",
                    "x86"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            externalNativeBuild {
                cmake {
                    cppFlags("-O3", "-DNDEBUG")
                }
            }
        }
        debug {
            externalNativeBuild {
                cmake {
                    cppFlags("-g", "-DDEBUG")
                }
            }
        }
    }

    // Enable ViewBinding for legacy views if needed
    buildFeatures {
        viewBinding = true
        prefab = true
        buildConfig = true
    }

    // Configure CMake for native code
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.29.2"
        }
    }

    // NDK version management
    ndkVersion = libs.versions.ndk.get()

    // Java and Kotlin compatibility
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_24.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-Xjvm-default=all"
        )
    }

    // Packaging options
    packaging {
        resources.excludes += setOf(
            "/META-INF/{AL2.0,LGPL2.1}",
            "/META-INF/AL2.0",
            "/META-INF/LGPL2.1"
        )
    }
}

dependencies {
    // Project modules
    implementation(project(":core-module"))
    implementation(project(":datavein-oracle-drive"))

    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:${libs.versions.espresso.get()}")
}

// Configure native build tasks
tasks.whenTaskAdded { task ->
    if (task.name.startsWith("externalNativeBuild")) {
        task.dependsOn(":copyNativeLibs")
    }
}

// Task to copy native libraries
tasks.register<Copy>("copyNativeLibs") {
    from("${project.rootDir}/native-libs")
    into("${buildDir}/native-libs")
    include("**/*.so")
    includeEmptyDirs = false
}
