// App module build configuration
@Suppress("DSL_SCOPE_VIOLATION") // False positive on version catalog access
plugins {
    // Core plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    // Hilt - Must be applied after Kotlin plugin
    alias(libs.plugins.hilt.android)
    
    // KSP
    alias(libs.plugins.ksp)
    
    // Kotlin plugins
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    
    // Other plugins
    id("org.openapi.generator") version "7.14.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("com.diffplug.spotless") version "6.25.0"
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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

    // Kotlin compiler options with K2
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24
            freeCompilerArgs.addAll(
                "-Xuse-k2",
                "-Xskip-prerelease-check",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.ExperimentalStdlibApi",
                "-opt-in=kotlin.contracts.ExperimentalContracts",
                "-Xjvm-default=all",
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
            )
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
    // Hilt
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
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    
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
    androidTestImplementation("androidx.test.espresso:espresso-core:${libs.versions.espresso.get()}")
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    
    // Hilt testing
    kspAndroidTest(libs.hilt.compiler.get())
}

// Detekt configuration
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    buildUponDefaultConfig = true
    allRules = false
    parallel = true
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    baseline.set(file("$rootDir/config/detekt/baseline.xml"))
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt/detekt.html"))
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

// KSP configuration
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

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
        licenseHeaderFile(rootProject.file("config/spotless/copyright.kt"))
    }
    format("misc") {
        target("**/*.md", "**/*.gradle", "**/*.toml")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
