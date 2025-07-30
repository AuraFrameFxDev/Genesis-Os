package dev.aurakai.auraframefx.buildlogic.convention

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            tasks.withType<Detekt>().configureEach {
                buildUponDefaultConfig = true
                allRules = false
                config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
                baseline.set(file("$rootDir/config/detekt/baseline.xml"))
                reports {
                    html.required.set(true)
                    html.outputLocation.set(file("build/reports/detekt/detekt.html"))
                    xml.required.set(false)
                    txt.required.set(false)
                    sarif.required.set(false)
                    md.required.set(false)
                }
            }
        }
    }
}
