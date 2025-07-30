plugins {
    id("dev.aurakai.auraframefx.buildlogic.convention.detekt")
    id("dev.aurakai.auraframefx.buildlogic.convention.spotless")
    alias(libs.plugins.kotlin.jvm)
}

group = "dev.aurakai"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))
}
