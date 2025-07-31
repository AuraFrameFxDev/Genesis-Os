plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `version-catalog`
}

// Set Java toolchain to version 24 for build logic
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    targetCompatibility = JavaVersion.VERSION_24
    sourceCompatibility = JavaVersion.VERSION_24
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        // Java 24 needs a compatible JVM target. JVM_21 is often sufficient.
        // Check Kotlin Gradle plugin documentation for specific JVM_24 support if needed.
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21) 
    }
}

// Configure Kotlin to use the same Java version for the compiler itself
kotlin {
    jvmToolchain(24) 
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    // Use version catalog references for dependencies used by buildSrc itself
dependencies {
    // Test dependencies for unit testing the build script
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(gradleTestKit())

 dependencies {
    implementation(libs.android.gradlePlugin) // Now refers to libs.android-gradlePlugin
    implementation(libs.kotlin.gradlePlugin) // Now refers to libs.kotlin-gradlePlugin
    implementation(libs.ksp.gradlePlugin) // Now refers to libs.ksp-gradlePlugin
    implementation(libs.hilt.android.gradlePlugin) // Now refers to libs.hilt-android-gradlePlugin
    implementation(libs.spotless.gradlePlugin) // Now refers to libs.spotless-gradlePlugin
    implementation(libs.detekt.gradlePlugin) // Now refers to libs.detekt-gradlePlugin
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
