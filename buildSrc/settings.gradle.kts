// Explicitly set the root project name for consistent build caching
rootProject.name = "buildSrc"

// Configure plugin management for buildSrc
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

// Configure dependency resolution for buildSrc
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
