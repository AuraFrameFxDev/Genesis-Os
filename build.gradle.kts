// MUST be first - before any plugins
System.setProperty("kotlin.native.disableTargets", "ios_arm32")
System.setProperty("org.jetbrains.kotlin.native.ignoreDisabledTargets", "true")

// Root build file - manages plugin versions and common configuration

// Configure Java toolchain for all projects
allprojects {
    plugins.withType<JavaBasePlugin> {
        configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(24))
                vendor.set(JvmVendorSpec.ADOPTIUM)
            }
        }
    }
}

// Configure Compose settings before plugins are applied
val enableCompose = true

plugins {
    // Only apply plugins that are not already applied in settings.gradle.kts or version catalog
    id("org.openapi.generator") version "7.14.0" apply false
    
    // Apply other plugins with `apply false` to make them available for subprojects
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
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
                targetSdk =(34)
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
                }
                sourceCompatibility = JavaVersion.VERSION_22
                targetCompatibility = JavaVersion.VERSION_22
            }
            
            // Configure Kotlin compilation for all source sets
            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_22)
                }
            }
            
            // Configure Java compilation for all source sets
            tasks.withType<JavaCompile>().configureEach {
                sourceCompatibility = JavaVersion.VERSION_22.toString()
                targetCompatibility = JavaVersion.VERSION_22.toString()
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
