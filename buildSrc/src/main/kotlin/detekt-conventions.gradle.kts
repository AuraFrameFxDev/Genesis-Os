import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    toolVersion = libs.findVersion("detekt").get().toString()
    config.setFrom(file("${rootDir}/config/detekt/detekt.yml"))
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}
