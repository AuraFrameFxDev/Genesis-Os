plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    id("org.openapi.generator")
    id("io.gitlab.arturbosch.detekt")
}

// Explicitly apply LifecycleBasePlugin for compatibility
apply(plugin = "base")

// Android configuration
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
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "24"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-k2",
            "-Xskip-prerelease-check",
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlin.contracts.ExperimentalContracts",
            "-Xjvm-default=all",
            "-Xexplicit-api=strict",
            "-progressive"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Configure OpenAPI Generator
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("${rootProject.projectDir}/openapi.yml")
    outputDir.set("${layout.buildDirectory.get()}/generated/openapi")
    configFile.set("${rootProject.projectDir}/openapi-generator-config.json")
    skipOverwrite.set(false)
    library.set("jvm-retrofit2")
    apiPackage.set("dev.aurakai.auraframefx.api.generated")
    modelPackage.set("dev.aurakai.auraframefx.model.generated")
    configOptions.set(mapOf(
        "useCoroutines" to "true",
        "serializationLibrary" to "kotlinx_serialization",
        "enumPropertyNaming" to "UPPERCASE"
    ))
}

// Add generated sources to the main source set
android.sourceSets.getByName("main") {
    java.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin"))
}

// Ensure OpenAPI generation happens before compilation
tasks.named("preBuild") {
    dependsOn("openApiGenerate")
}


// Configure OpenAPI Generator for the app module
project(":app").afterEvaluate {
    apply(plugin = "org.openapi.generator")

    configure<org.openapitools.generator.gradle.plugin.extensions.OpenApiGeneratorGenerateExtension> {
        generatorName.set("kotlin")
        inputSpec.set("${rootProject.projectDir}/openapi.yml")
        outputDir.set("${layout.buildDirectory.get()}/generated/openapi")
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
}


// Configure OpenAPI generation
project(":app").openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set(rootProject.layout.projectDirectory.file("openapi.yml").asFile.path)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.path)
    configFile.set(rootProject.layout.projectDirectory.file("openapi-generator-config.json").asFile.path)
    skipOverwrite.set(false)
    library.set("jvm-retrofit2")
    apiPackage.set("dev.aurakai.auraframefx.api.generated")
    modelPackage.set("dev.aurakai.auraframefx.model.generated")
    configOptions.set(mapOf(
        "useCoroutines" to "true",
        "serializationLibrary" to "kotlinx_serialization",
        "enumPropertyNaming" to "UPPERCASE"
    ))
}

// Add generated sources to the main source set
project(":app").android.sourceSets.getByName("main") {
    java.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin"))
}

// Ensure OpenAPI generation happens before compilation
project(":app").tasks.named("preBuild") {
    dependsOn("openApiGenerate")
}

// Android configuration
project(":app").android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 36
    
    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
        isCoreLibraryDesugaringEnabled = true
    }
    
    kotlinOptions {
        jvmTarget = "24"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-k2",
            "-Xskip-prerelease-check",
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlin.contracts.ExperimentalContracts",
            "-Xjvm-default=all"
        )
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"
        }
    }
}

dependencies {
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
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)
    
    // Coil
    implementation(libs.coil.compose)
    
    // Timber
    implementation(libs.timber)
    
    // Core library desugaring
    coreLibraryDesugaring(libs.android.desugar.jdk.libs)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Detekt configuration for static analysis
detekt {
    toolVersion = "1.22.0"
    config = files("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/detekt/baseline.xml")
    reports {
        xml.enabled = false
        html.enabled = true
        txt.enabled = false
    }
}
