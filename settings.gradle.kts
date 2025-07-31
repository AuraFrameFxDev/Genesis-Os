@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Genesis-Os"

// Your dynamic module inclusion logic is excellent.
// All modules from your project are included here.
include(
    ":app",
    ":feature-module",
    ":core-module",
    ":collab-canvas",
    ":colorblendr",
    ":secure-comm",
    ":datavein-oracle-drive",
    ":datavein-oracle-native",
    ":sandbox-ui",
    ":oracledrive"
)