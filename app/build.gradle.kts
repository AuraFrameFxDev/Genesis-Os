// App module build configuration
@Suppress("DSL_SCOPE_VIOLATION") // False positive on version catalog access
plugins {
    // Core plugins - apply directly with versions from version catalog
    id("com.android.application")
    
    // Kotlin plugins
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
    id("com.google.devtools.ksp")

    // Hilt
    id("com.google.dagger.hilt.android")

    // Other plugins
    id("org.openapi.generator") version "7.14.0"
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        
        vectorDrawables {
            useSupportLibrary = true
        }
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
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    // Build features
    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    // Compose options
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    // Packaging options
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1",
                "META-INF/LICENSE*",
                "META-INF/NOTICE*",
                "META-INF/*.version",
                "META-INF/proguard/*",
                "/*.properties",
                "fabric/*.properties",
                "DebugProbesKt.bin"
            )
        }
    }

    // Java toolchain
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(24))
        }
    }

    // Test options
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    openApiGenerate {
        generatorName.set("kotlin")
        inputSpec.set("${rootProject.projectDir}/openapi.yml")
        outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.path)
        configFile.set("${rootProject.projectDir}/openapi-generator-config.json")
        skipOverwrite.set(false)
        library.set("jvm-retrofit2")
        apiPackage.set("dev.aurakai.auraframefx.api.generated")
        modelPackage.set("dev.aurakai.auraframefx.model.generated")
        configOptions.set(
            mapOf(
                "useCoroutines" to "true",
                "serializationLibrary" to "kotlinx_serialization",
                "enumPropertyNaming" to "UPPERCASE"
            )
        )
    }

    // Add generated sources to the main source set
    sourceSets.getByName("main") {
        java.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin"))
    }

    // Ensure OpenAPI generation happens before compilation
    tasks.named("preBuild") {
        dependsOn("openApiGenerate")
    }
}

// Dependencies block OUTSIDE android {}
dependencies {
    // Hilt (remove redundant second instance)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp3.logging.interceptor)
    
    // UI
    implementation(libs.coil.compose)
    implementation(libs.timber)
    
    // Core Library Desugaring
    coreLibraryDesugaring(libs.coreLibraryDesugaring)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    
    // Hilt testing
    kspAndroidTest("com.google.dagger:hilt-compiler:2.57")
}


// Place KSP arguments outside android block
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

// Java compilation
tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_24.toString()
    targetCompatibility = JavaVersion.VERSION_24.toString()
    options.encoding = "UTF-8"
    options.isIncremental = true
}

// Test tasks
tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
