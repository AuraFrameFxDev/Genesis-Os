import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

plugins {
    id("io.gitlab.arturbosch.detekt")
}

// Configure Detekt for all projects
configure<DetektExtension> {
    // Use version from version catalog
    toolVersion = libs.versions.detekt.get()
    
    // Use the default config file from root project
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    
    // Build upon the default config
    buildUponDefaultConfig = true
    
    // Enable all available (but not necessarily all) rules
    allRules = false
    
    // Enable parallel execution
    parallel = true
    
    // Enable build failure on any finding with priority "error"
    buildUponDefaultConfig = true
    
    // Disable auto-correct (use ktlint for formatting)
    autoCorrect = false
    
    // Enable type resolution
    debug {
        // Enable to see the AST of the file being analyzed
        // Use when reporting an issue with detekt
        report = System.getProperty("detekt.debug", "false").toBoolean()
    }
}

// Configure dependencies
dependencies {
    // Use the version catalog for detekt plugins
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.compose)
    
    // Add custom rules if needed
    // detektPlugins("your:custom-detekt-rules:1.0.0")
}

// Configure tasks
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    // Target version of the generated JVM bytecode
    jvmTarget = JavaVersion.VERSION_17.toString()
    
    // Enable parallel compilation of detekt tasks
    parallel = true
    
    // Enable type resolution
    buildUponDefaultConfig = true
    
    // Enable all available (but not necessarily all) rules
    allRules = false
    
    // Target Java 17 bytecode
    jvmTarget = "17"
    
    // Enable auto-correction for the following checks
    // autoCorrect is false by default
    
    // Ignore test files
    ignoreFailures = false
    
    // Enable/disable the parallel execution of detekt
    // Only makes sense if parallel execution is enabled for the Gradle project
    parallel = true
    
    // Specify the base path for file paths in the output
    // If not set, the directory containing the config file will be used
    // basePath = project.rootDir.absolutePath
    
    // Enable debug mode
    debug = System.getProperty("detekt.debug", "false").toBoolean()
    
    // Ignore failures
    // Useful for CI builds
    ignoreFailures = false
    
    // Build reports in subprojects folder
    // This is a workaround for the fact that the reports in the root project are overwritten
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        
        // Custom report paths
        html.outputLocation.set(file("build/reports/detekt/detekt.html"))
        xml.outputLocation.set(file("build/reports/detekt/detekt.xml"))
        txt.outputLocation.set(file("build/reports/detekt/detekt.txt"))
        sarif.outputLocation.set(file("build/reports/detekt/detekt.sarif"))
    }
}
    
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
    }
}

// Ensure Detekt uses the same Kotlin version as the project
// This helps avoid version conflicts
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_21.toString()
}
