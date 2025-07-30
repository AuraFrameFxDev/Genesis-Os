@file:Suppress("UnstableApiUsage", "JCenterRepository")

// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    toolchainManagement {
        jvm {
            javaRepositories {
                repository("foojay") {
                    resolverClass.set(org.gradle.platform.toolchain.foojay.FoojayToolchainResolver::class.java)
                }
            }
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Set the root project name to match your repository/project
rootProject.name = "Genesis-Os"

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
        logger.lifecycle("Skipping non-existent module: $modulePath")
    }
}

// Configure all included projects
rootProject.children.forEach { project ->
    project.projectDir = file(project.name)
}