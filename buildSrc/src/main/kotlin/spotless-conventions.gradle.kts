import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.diffplug.spotless")
}

spotless {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    kotlin {
        target("src/**/*.kt")
        ktlint(libs.findVersion("ktlint").get().toString())
    }
}
