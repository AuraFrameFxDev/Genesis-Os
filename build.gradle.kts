// Root build file - manages plugin versions and common configuration

// Disable iOS ARM32 target which is not supported by recent Compose/Kotlin versions
System.setProperty("kotlin.native.ignoreDisabledTargets", "true")
System.setProperty("kotlin.native.disableTargets", "ios_arm32")

// Configure Compose settings before plugins are applied
val enableCompose = true

plugins {
    // Android plugins
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    // Kotlin plugins
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // KSP (Kotlin Symbol Processing)
    alias(libs.plugins.ksp) apply false

    // Hilt for dependency injection
    alias(libs.plugins.hilt.android) apply false

    // Other plugins
    id("org.openapi.generator") version "7.14.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.5" apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    
    // Compose Multiplatform plugin (applied in settings.gradle.kts)
    kotlin("multiplatform") version "2.2.0" apply false
}

// Common configuration for all subprojects
subprojects {
    // Configure Kotlin compiler options for all Kotlin projects
    plugins.withId("org.jetbrains.kotlin.android") {
        configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension> {
            compilerOptions {
                // Enable K2 compiler
                freeCompilerArgs.addAll(
                    "-Xuse-k2",
                    "-opt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
    
    // Configure Android projects
    pluginManager.withPlugin("com.android.application") {
        configure<com.android.build.gradle.BaseExtension> {
            compileSdkVersion(36)

            defaultConfig {
                minSdk = 33
                targetSdk = 36
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            // Configure Compose for Android projects
            buildFeatures.compose = true

            composeOptions {
                kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
            }
        }
    }
    pluginManager.withPlugin("com.android.library") {
        configure<com.android.build.gradle.BaseExtension> {
            compileSdkVersion(36)

            defaultConfig {
                minSdk = 33
                targetSdk = 36
            }
            
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            
            // Configure Compose for Android projects
            buildFeatures.compose = true
            
            composeOptions {
                kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
            }
        }
    }
    
    // Configure all projects including the root project
    allprojects {
        // Configure Java toolchain for all projects
        plugins.withType<JavaBasePlugin> {
            configure<JavaPluginExtension> {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(24))
                    // Remove vendor specification to allow any JDK 24 implementation
                    // vendor.set(JvmVendorSpec.AZUL)
                }
            }
            
            // Explicitly set Java toolchain for all source sets
            afterEvaluate {
                tasks.withType<JavaCompile>().configureEach {
                    sourceCompatibility = JavaVersion.VERSION_24.toString()
                    targetCompatibility = JavaVersion.VERSION_24.toString()
                }
            }
        }

        // Configure Kotlin compilation for all projects
        plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin> {
            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
                    apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
                    languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)

                    // Enable K2 compiler and other compiler options
                    freeCompilerArgs.addAll(
                        "-Xjvm-default=all",
                        "-opt-in=kotlin.RequiresOptIn",
                        "-opt-in=kotlin.Experimental",
                        "-Xuse-k2",
                        "-Xexplicit-api=strict",
                        "-progressive",
                        "-Xenhance-type-parameter-types-to-def-not-null",
                        "-Xjsr305=strict"
                    )
                }
            }
        }
    }
}

// Register clean task only at the root level
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
    delete("$rootDir/build/")
    delete("$rootDir/app/build/")
}
