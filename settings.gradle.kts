// Settings configured for Gradle 8.14.3 and Java 24
@file:Suppress("UnstableApiUsage", "JCenterRepository")


// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

// Disable iOS ARM32 which is not supported by recent Compose/Kotlin versions
System.setProperty("kotlin.native.ignoreDisabledTargets", "true")
System.setProperty("kotlin.native.disableTargets", "ios_arm32")
System.setProperty("org.jetbrains.kotlin.native.ignoreDisabledTargets", "true")
System.setProperty("org.jetbrains.compose.experimental.uikit.enabled", "false")

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
    resolutionStrategy {
        eachPlugin {
            when {
                requested.id.namespace == "com.android" ->
                    useModule("com.android.tools.build:gradle:${requested.version}")

                requested.id.id == "com.google.devtools.ksp" ->
                    useModule("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${requested.version}")

                requested.id.id == "com.google.dagger.hilt.android" ->
                    useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")

                requested.id.id == "org.jetbrains.compose" ->
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