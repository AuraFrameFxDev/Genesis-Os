// Settings configured for Gradle 8.14.3 and Java 24
@file:Suppress("UnstableApiUsage", "JCenterRepository")

// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

// Disable iOS ARM32 which is not supported by recent Compose/Kotlin versions
System.setProperty("kotlin.native.ignoreDisabledTargets", "true")
System.setProperty("kotlin.native.disableTargets", "ios_arm32")
System.setProperty("org.jetbrains.kotlin.native.ignoreDisabledTargets", "true")

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
                    
                // Note: Removed Compose Multiplatform plugin configuration as we're using standard Android Compose
                // Compose Multiplatform is not compatible with our current setup
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

// List of all modules that actually exist in the project
val modules = listOf(
    ":app",
    ":feature-module",
    ":core-module",
    ":colleb-canvas",
    ":colorblendr",
    ":secure-comm"
)

// Only include modules that exist in the filesystem
modules.forEach { modulePath ->
    val moduleDir = file(modulePath.removePrefix(":"))
    if (moduleDir.exists() && moduleDir.isDirectory) {
        include(modulePath)
    } else {
        logger.warn("Skipping non-existent module: $modulePath")
    }
}

// Configure all included projects
rootProject.children.forEach { project ->
    project.projectDir = file(project.name)
}