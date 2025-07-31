plugins {
    id("room-database-convention")
    id("dev.aurakai.auraframefx.buildlogic.convention.hilt")
}

android {
    namespace = "dev.aurakai.auraframefx.oracledrive"
}

dependencies {
    // Project modules
    implementation(project(":core-module"))

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.core.testing)
    testRuntimeOnly(libs.junit.engine)

    // Android Instrumentation Tests
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}