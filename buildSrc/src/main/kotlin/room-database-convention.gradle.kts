import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

plugins {
    // This plugin builds on the base library conventions
    id("android-library-conventions")
}

dependencies {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    
    // Add Room libraries using version catalog
    implementation(libs.findLibrary("room.runtime").get())
    implementation(libs.findLibrary("room.ktx").get())
    
    // This is the crucial line: use "ksp" for the Room compiler
    ksp(libs.findLibrary("room.compiler").get())
    
    // Optional: Room testing support
    testImplementation(libs.findLibrary("room.testing").get())
}

android {
    // Room schema export configuration
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }
}
