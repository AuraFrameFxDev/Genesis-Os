package dev.aurakai.auraframefx.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.dagger.hilt.android")
            // ksp is applied in the buildlogic
            dependencies {
                "implementation"("com.google.dagger:hilt-android:2.57")
                "ksp"("com.google.dagger:hilt-compiler:2.57")
            }
        }
    }
}
