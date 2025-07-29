// This script disables the problematic iOS ARM32 target for Kotlin Multiplatform
// It needs to be applied before any plugins that might trigger the Compose plugin

// Set system properties before any build logic runs
System.setProperty("kotlin.native.ignoreDisabledTargets", "true")
System.setProperty("kotlin.native.disableTargets", "ios_arm32")

// Apply to all projects
allprojects {
    // This will run after the project is evaluated
    afterEvaluate {
        // Check if this is a Kotlin Multiplatform project
        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            // Configure Kotlin Multiplatform extension if it exists
            extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()?.apply {
                // Explicitly skip the problematic target
                targets.all {
                    if (this is org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget) {
                        if (konanTarget.name == "ios_arm32") {
                            logger.lifecycle("Skipping unsupported target: ios_arm32")
                            this.enabled = false
                        }
                    }
                }
            }
        }
    }
}
