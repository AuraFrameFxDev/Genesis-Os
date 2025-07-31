plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
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
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
    }
}

// Configure Kotlin to use the same Java version
kotlin {
    jvmToolchain(24)
}

dependencies {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    // Use version catalog references
    implementation(libs.findPlugin("android.application").get())
    implementation(libs.findPlugin("kotlin.android").get())
    implementation(libs.findPlugin("ksp").get())
    implementation(libs.findPlugin("hilt.android").get())
    implementation(libs.findLibrary("spotless.gradle.plugin").get())
    implementation(libs.findLibrary("detekt.gradle.plugin").get())
}
