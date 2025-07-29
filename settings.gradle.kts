// Settings configured for Gradle 8.14.3 and Java 24
@file:Suppress("UnstableApiUsage", "JCenterRepository")

// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

// Plugin Management
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }

    // Configure resolution strategy for plugins
    resolutionStrategy {
        eachPlugin {
            when {
                // Android plugins
                requested.id.namespace == "com.android" ->
                    useModule("com.android.tools.build:gradle:${requested.version}")

                // KSP plugin
                requested.id.id == "com.google.devtools.ksp" ->
                    useModule("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${requested.version}")

                // Hilt plugin
                requested.id.id == "com.google.dagger.hilt.android" ->
                    useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
            }
        }
    }
}

// Dependency resolution management
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }
}

// Include all modules
rootProject.name = "AuraFrameFX"
include(":presentation-module")
include(":app")
include(":feature-module")
include(":core-module")
include(":colleb-canvas")
include(":colorblendr")
include(":colorblendr-compose")
include(":colorblendr-compose:colorblendr-compose-gradle-plugin")

// Configure all projects
rootProject.children.forEach { project ->
    project.projectDir = file(project.name)
}