@file:Suppress("UnstableApiUsage", "JCenterRepository")

// 3. pluginManagement block for plugin repositories and plugin versions
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

// 4. dependencyResolutionManagement for dependency repositories
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// 5. Root project name and module includes
rootProject.name = "Genesis-Os"
include(":app", ":sandbox-ui", ":collab-canvas", ":oracledrive")

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