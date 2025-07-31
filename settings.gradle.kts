// Settings configured for Gradle 8.14.3 and Java 24
@file:Suppress("UnstableApiUsage", "JCenterRepository")


// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")


// Configure plugin resolution
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
    
    // Use version catalog for all plugin versions
    plugins {
        id("com.android.application") version "8.11.1"
        id("org.jetbrains.kotlin.android") version "2.2.0"
        id("com.google.devtools.ksp") version "2.2.0-2.0.2"
        id("com.google.dagger.hilt.android") version "2.57"
    }
    
    resolutionStrategy {
        eachPlugin {
            when {
                // Only resolve plugins that aren't already handled by the version catalog
                requested.id.namespace == "org.jetbrains.compose" ->
                    useModule("org.jetbrains.compose:compose-gradle-plugin:${requested.version}")
            }
        }
    }
}

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
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
        flatDir {
            dirs("${rootDir}/libs", "${rootDir}/app/libs")
        }
    }
}

// Create the libs directory if it doesn't exist
val libsDir = file("${rootDir}/libs")
if (!libsDir.exists()) {
    libsDir.mkdirs()
}

// Set the root project name to match your repository/project
rootProject.name = "Genesis-Os"

// List of all modules that actually exist in the project
val modules = listOf(
    ":app",
    ":feature-module",
    ":core-module",
    ":collab-canvas",
    ":colorblendr",
    ":secure-comm"
)

// Only include modules that exist in the filesystem
modules.forEach { modulePath ->
    val moduleDir = file(modulePath.removePrefix(":"))
    if (moduleDir.exists() && moduleDir.isDirectory) {
        include(modulePath)
    } else {
        logger.lifecycle("Skipping non-existent module: $modulePath")
    }
}

// Configure all included projects
rootProject.children.forEach { project ->
    project.projectDir = file(project.name)
}