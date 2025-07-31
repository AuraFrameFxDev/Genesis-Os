import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// Common configuration for core modules
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("org.jetbrains.dokka")
}

// Configure Android options
configure<LibraryExtension> {
    namespace = "com.genesisos.core.${project.name.lowercase()}"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = project.name != "domain"  // Only enable compose for UI modules
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    // Enable Java 17 bytecode
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-k2",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}

// Configure Kotlin options
configure<KotlinAndroidProjectExtension> {
    jvmToolchain(17)
}

// Dependencies
dependencies {
    // Core AndroidX
    implementation(libs.bundles.androidx.core)

    // Compose (only for UI modules)
    if (project.name != "domain" && project.name != "data") {
        implementation(platform(libs.compose.bom))
        implementation(libs.bundles.compose.essentials)
        debugImplementation(libs.compose.ui.tooling)
    }

    // Hilt (only for modules that need dependency injection)
    if (project.name != "ui" && project.name != "common") {
        implementation(libs.hilt.android)
        kapt(libs.hilt.compiler)
    }

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose testing (only for UI modules)
    if (project.name != "domain" && project.name != "data") {
        androidTestImplementation(platform(libs.compose.bom))
        androidTestImplementation(libs.compose.ui.test.junit4)
        debugImplementation(libs.compose.ui.test.manifest)
    }
}

// KAPT options
kapt {
    correctErrorTypes = true
    useBuildCache = true
}

// Dokka configuration
tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka"))

    moduleName.set("${rootProject.name}-${project.name}")

    dokkaSourceSets {
        configureEach {
            includes.from("$projectDir/README.md")

            // Display inherited members
            skipDeprecated.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)

            // Source links
            sourceLink {
                localDirectory.set(file("$projectDir/src/main/kotlin"))
                remoteUrl.set(
                    uri(
                        "https://github.com/your-org/Genesis-Os/tree/main/${
                            project.path.replace(
                                ":",
                                "/"
                            )
                        }/src/main/kotlin"
                    ).toURL()
                )
                remoteLineSuffix = "#L"
            }
        }
    }
}
