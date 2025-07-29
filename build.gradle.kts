// Root build file - manages plugin versions and common configuration
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
}

// Common configuration for all subprojects
subprojects {
    // Configure Java toolchain for all Java projects
// Configure all projects
    allprojects {
        // Configure Java toolchain for all projects
        plugins.withType<JavaBasePlugin> {
            configure<JavaPluginExtension> {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(24))
                    vendor.set(JvmVendorSpec.AZUL)
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


    tasks.register<Delete>("clean") {
        delete(rootProject.layout.buildDirectory)
        delete("$rootDir/build/")
        delete("$rootDir/app/build/")
    }
}

