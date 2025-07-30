# Add Kotlin Multiplatform configuration to disable iOS ARM32
targets.all {
    if (it is org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget) {
        it.binaries.all {
            // Disable iOS ARM32
            if (it.target.architecture.name == "arm32" && it.target.family == org.jetbrains.kotlin.konan.target.Family.IOS) {
                it.compilations.all { compilation ->
                    compilation.kotlinOptions.freeCompilerArgs += "-Xskip-prerelease-check"
                }
            }
        }
    }
}

// Add to all projects
allprojects {
    afterEvaluate {
        // Disable iOS ARM32 for all Kotlin Multiplatform targets
        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            kotlin {
                targets.all { target ->
                    if (target.platformType == org.jetbrains.kotlin.gradle.plugin.mpp.NativePlatformType.ANDROID_NATIVE) {
                        target.attributes.attribute(org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.attribute, "native")
                    }
                }
            }
        }
    }
}
