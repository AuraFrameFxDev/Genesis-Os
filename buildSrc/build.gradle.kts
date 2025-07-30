import org.gradle.api.JavaVersion
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    id("org.jetbrains.dokka") version "1.9.20"
    id("com.gradle.plugin-publish") version "1.2.0"
    id("com.autonomousapps.dependency-analysis") version "1.30.0"
}

// Configure Java toolchain for buildSrc to use Java 21
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor = JvmVendorSpec.AZUL
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Kotlin plugins - using string notation to avoid early catalog access
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    
    // Android plugins
    implementation("com.android.tools.build:gradle:8.1.1")
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    
    // Build tools
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.0-2.0.2")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.57")
    implementation("androidx.room:room-compiler:2.7.2")
    
    // OpenAPI Generator
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.5.0")
    
    // Testing
    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("com.google.truth:truth:1.1.5")
}

// Configure Kotlin compilation with K2
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
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

// Task to generate a report of all tasks in the build
tasks.register("taskReport") {
    group = "Help"
    description = "Generates a report of all available tasks"
    
    doLast {
        val tasks = project.tasks.sortedBy { it.name }
        val maxLength = tasks.maxOf { it.name.length } + 4
        
        println("\n=== Available Tasks ===\n")
        tasks.forEach { task ->
            println("${'$'}{task.name.padEnd(maxLength)} - ${'$'}{task.description ?: 'No description'}")
        }
    }
}