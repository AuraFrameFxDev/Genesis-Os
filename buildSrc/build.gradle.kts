// buildSrc/build.gradle.kts

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `version-catalog`
    kotlin("jvm") version "2.2.0" // Ensure this is Kotlin 2.2.0 or newer
}

// Configure repositories where Gradle will look for dependencies and plugins
repositories {
    gradlePluginPortal() // For Gradle plugins
    google() // For Android-specific dependencies and plugins (e.g., AGP, Hilt)
    mavenCentral() // For general Maven artifacts
}

// Set Java toolchain to version 24 for build logic compilation
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    // These are automatically handled by the toolchain but can be explicitly set if needed
    // targetCompatibility = JavaVersion.VERSION_24
    // sourceCompatibility = JavaVersion.VERSION_24
}

// Configure Kotlin to use the same Java version for the compiler itself
kotlin {
    jvmToolchain(24) // Sets the JVM toolchain to Java 24
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

// Dependencies for the buildSrc project itself (i.e., plugins and extensions used by the buildSrc)
dependencies {
    // Test dependencies for unit testing the build script
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(gradleTestKit())

    // Implementation dependencies for buildSrc plugins/logic
    // These should typically be plugin artifacts or their associated libraries
    implementation("com.android.tools.build:gradle:8.11.1") // Android Gradle Plugin
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0") // Kotlin Gradle Plugin
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.2.0-2.0.2") // KSP Plugin
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.57") // Hilt Plugin
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.25.0") // Spotless Plugin
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.6") // Detekt Plugin
}

tasks.test {
    useJUnitPlatform()

    // Configure test execution
    testLogging {
        events("passed", "skipped", "failed")
    }

    // Set system properties for tests
    systemProperty("gradle.test.kit.debug", "false")
}
