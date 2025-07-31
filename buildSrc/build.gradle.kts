plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

// Set Java toolchain to version 24 for build logic but target JVM 22
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    targetCompatibility = JavaVersion.VERSION_22
    sourceCompatibility = JavaVersion.VERSION_22
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_22)
    }
}

// Configure Kotlin to use the same Java version
kotlin {
    jvmToolchain(24)
}

// Define plugin versions
val kotlinVersion = "2.2.0"
val agpVersion = "8.11.1"
val spotlessVersion = "6.25.0"
val detektVersion = "1.23.6"  // Latest stable Detekt version
val kspVersion = "2.2.0-2.0.2" // Example KSP version, adjust as needed
val hiltVersion = "2.57" // Example Hilt version, adjust as needed

dependencies {
    // Android Gradle Plugin
    implementation("com.android.tools.build:gradle:$agpVersion")
    
    // Kotlin Gradle Plugin (using the marker artifact convention for plugins)
    implementation("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:$kotlinVersion")
    
    // KSP Plugin
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$kspVersion")
    
    // Hilt Plugin
    implementation("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    
    // Spotless Plugin
    implementation("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
    
    // Detekt Plugin
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detektVersion")
}
