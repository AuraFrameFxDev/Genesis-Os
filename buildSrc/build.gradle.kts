import org.gradle.api.JavaVersion
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    `kotlin-dsl` version "4.4.0"
    `kotlin-dsl-precompiled-script-plugins`
    id("org.jetbrains.dokka") version "1.9.20"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.autonomousapps.dependency-analysis") version "1.30.0"
}

// Configure Java toolchain for buildSrc to use Java 17
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Updated versions for compatibility
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    
    // Updated Android Gradle Plugin
    implementation("com.android.tools.build:gradle:8.7.3") // Updated from 8.1.1
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.5")
    
    // Build tools - updated versions
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.0-2.0.2")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.57")
    implementation("androidx.room:room-compiler:2.7.2")
    
    // Updated OpenAPI Generator
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.14.0")
    
    // Testing
    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("com.google.truth:truth:1.4.4")
}

// FIXED: Use Java 17 instead of 21 for broader compatibility
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17) // Changed from JVM_21
        freeCompilerArgs.addAll(
            "-Xuse-k2",
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlin.contracts.ExperimentalContracts",
            "-Xcontext-receivers"
        )
    }
}

// Configure Dokka for buildSrc documentation
tasks.dokkaHtml {
    outputDirectory.set(file("${'$'}{project.layout.buildDirectory.get()}/dokka"))
    moduleName.set("Genesis-OS Build Logic")

    dokkaSourceSets {
        configureEach {
            includes.from("module.md")
            skipDeprecated.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)

            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(uri("https://github.com/your-org/Genesis-Os/tree/main/buildSrc/src/main/kotlin").toURL())
                remoteLineSuffix = "#L"
            }
        }
    }
}

// Configure dependency analysis
dependencyAnalysis {
    issues {
        all {
            onAny { severity("warn") }
            onUnusedDependencies { severity("warn") }
            onIncorrectConfiguration { severity("warn") }
            onRedundantPlugins { severity("warn") }
        }
    }
}

// Configure test tasks
tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showExceptions = true
        showStackTraces = true
        showCauses = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
    outputs.cacheIf { true }
    systemProperty("gradle.user.home", file("${'$'}{project.layout.buildDirectory.get()}/test-home"))
}

// FIXED: Remove unused variables to eliminate warnings
tasks.register("taskReport") {
    group = "Help"
    description = "Generates a report of all available tasks"
    
    doLast {
        val tasks = project.tasks.sortedBy { it.name }
        val nameWidth = tasks.maxOfOrNull { it.name.length }?.plus(4) ?: 20
        
        println("\n=== Available Tasks ===\n")
        tasks.forEach { task ->
            println("${task.name.padEnd(nameWidth)} - ${task.description ?: "No description"}")
        }
    }
}