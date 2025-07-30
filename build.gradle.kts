// build.gradle.kts - ROOT (NO BUILDSCRIPT!)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.dokka)
}

// Subprojects configuration
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")
    
    detekt {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
    }
    
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint("1.0.1")
            endWithNewline()
            trimTrailingWhitespace()
        }
    }
}
