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

dependencies {
    // Test dependencies for unit testing the build script
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(gradleTestKit())

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    // Use version catalog references for dependencies used by buildSrc itself
    implementation(libs.findPlugin("android.application").get())
    implementation(libs.findPlugin("kotlin.android").get())
    implementation(libs.findPlugin("ksp").get())
    implementation(libs.findPlugin("hilt.android").get())
    implementation(libs.findLibrary("spotless.gradle.plugin").get())
    implementation(libs.findLibrary("detekt.gradle.plugin").get())
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
