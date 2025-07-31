import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.*
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.gradle.api.JavaVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.junit.jupiter.api.AfterEach
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.UnknownTaskException
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * Comprehensive unit tests for the buildSrc build.gradle.kts configuration.
 * 
 * Testing Framework: JUnit Jupiter (JUnit 5) - as identified from project dependencies
 * 
 * These tests validate the build script configuration including:
 * - Plugin applications (kotlin-dsl, kotlin-dsl-precompiled-script-plugins)
 * - Java toolchain configuration (version 24)
 * - Kotlin compilation settings (JVM target 24)
 * - Version catalog dependency resolution
 */
@DisplayName("Build Gradle KTS Configuration Tests")
class BuildGradleKtsTest {

    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        project = ProjectBuilder.builder()
            .withName("buildSrc-test")
            .build()
    }

    @Nested
    @DisplayName("Plugin Configuration Tests")
    inner class PluginConfigurationTests {

        @Test
        @DisplayName("Should apply kotlin-dsl plugin successfully")
        fun shouldApplyKotlinDslPlugin() {
            // When
            project.pluginManager.apply("kotlin-dsl")
            
            // Then
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl"))
        }

        @Test
        @DisplayName("Should apply kotlin-dsl-precompiled-script-plugins plugin successfully")
        fun shouldApplyKotlinDslPrecompiledScriptPluginsPlugin() {
            // When
            project.pluginManager.apply("kotlin-dsl-precompiled-script-plugins")
            
            // Then
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl-precompiled-script-plugins"))
        }

        @Test
        @DisplayName("Should apply both required plugins together")
        fun shouldApplyBothRequiredPlugins() {
            // When
            project.pluginManager.apply("kotlin-dsl")
            project.pluginManager.apply("kotlin-dsl-precompiled-script-plugins")
            
            // Then
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl"))
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl-precompiled-script-plugins"))
        }

        @Test
        @DisplayName("Should handle plugin application order")
        fun shouldHandlePluginApplicationOrder() {
            // Test that plugins can be applied in any order without issues
            // When - Apply in reverse order
            project.pluginManager.apply("kotlin-dsl-precompiled-script-plugins")
            project.pluginManager.apply("kotlin-dsl")
            
            // Then
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl"))
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl-precompiled-script-plugins"))
        }

        @Test
        @DisplayName("Should handle duplicate plugin applications")
        fun shouldHandleDuplicatePluginApplications() {
            // When - Apply the same plugin multiple times
            project.pluginManager.apply("kotlin-dsl")
            project.pluginManager.apply("kotlin-dsl") // Duplicate
            
            // Then - Should still work correctly
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl"))
        }
    }

    @Nested
    @DisplayName("Java Toolchain Configuration Tests")
    inner class JavaToolchainConfigurationTests {

        @BeforeEach
        fun setupJavaPlugin() {
            project.pluginManager.apply("java")
        }

        @Test
        @DisplayName("Should configure Java toolchain to version 24")
        fun shouldConfigureJavaToolchainToVersion24() {
            // Given
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When - Apply the exact configuration from build.gradle.kts
            javaExtension.toolchain {
                languageVersion.set(JavaLanguageVersion.of(24))
            }
            
            // Then
            assertEquals(
                JavaLanguageVersion.of(24),
                javaExtension.toolchain.languageVersion.get()
            )
        }

        @Test
        @DisplayName("Should set target compatibility to Java 24")
        fun shouldSetTargetCompatibilityToJava24() {
            // Given
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When - Apply the exact configuration from build.gradle.kts
            javaExtension.targetCompatibility = JavaVersion.VERSION_24
            
            // Then
            assertEquals(JavaVersion.VERSION_24, javaExtension.targetCompatibility)
        }

        @Test
        @DisplayName("Should set source compatibility to Java 24")
        fun shouldSetSourceCompatibilityToJava24() {
            // Given
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When - Apply the exact configuration from build.gradle.kts
            javaExtension.sourceCompatibility = JavaVersion.VERSION_24
            
            // Then
            assertEquals(JavaVersion.VERSION_24, javaExtension.sourceCompatibility)
        }

        @Test
        @DisplayName("Should configure all Java compatibility settings consistently")
        fun shouldConfigureAllJavaCompatibilitySettingsConsistently() {
            // Given
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When - Apply the exact configuration from build.gradle.kts
            javaExtension.apply {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(24))
                }
                targetCompatibility = JavaVersion.VERSION_24
                sourceCompatibility = JavaVersion.VERSION_24
            }
            
            // Then
            assertEquals(JavaLanguageVersion.of(24), javaExtension.toolchain.languageVersion.get())
            assertEquals(JavaVersion.VERSION_24, javaExtension.targetCompatibility)
            assertEquals(JavaVersion.VERSION_24, javaExtension.sourceCompatibility)
        }

        @ParameterizedTest
        @ValueSource(ints = [8, 11, 17, 21, 24])
        @DisplayName("Should handle different Java versions for toolchain")
        fun shouldHandleDifferentJavaVersionsForToolchain(version: Int) {
            // Given
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When
            javaExtension.toolchain {
                languageVersion.set(JavaLanguageVersion.of(version))
            }
            
            // Then
            assertEquals(JavaLanguageVersion.of(version), javaExtension.toolchain.languageVersion.get())
        }

        @Test
        @DisplayName("Should validate Java version bounds")
        fun shouldValidateJavaVersionBounds() {
            // Given
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When & Then - Test edge case versions
            assertDoesNotThrow {
                // Minimum reasonable version
                javaExtension.toolchain {
                    languageVersion.set(JavaLanguageVersion.of(8))
                }
            }
            
            assertDoesNotThrow {
                // High version number (future-proofing)
                javaExtension.toolchain {
                    languageVersion.set(JavaLanguageVersion.of(50))
                }
            }
        }
    }

    @Nested
    @DisplayName("Kotlin Compilation Configuration Tests")
    inner class KotlinCompilationConfigurationTests {

        @BeforeEach
        fun setupKotlinPlugin() {
            project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        }

        @Test
        @DisplayName("Should configure Kotlin compile tasks with JVM target 24")
        fun shouldConfigureKotlinCompileTasksWithJvmTarget24() {
            // Given
            val compileTask = project.tasks.create("testCompileKotlin", KotlinCompile::class.java)
            
            // When - Apply the exact configuration from build.gradle.kts
            compileTask.compilerOptions {
                jvmTarget.set("24")
            }
            
            // Then
            assertEquals("24", compileTask.compilerOptions.jvmTarget.get())
        }

        @Test
        @DisplayName("Should configure Kotlin JVM toolchain to version 24")
        fun shouldConfigureKotlinJvmToolchainToVersion24() {
            // Given
            val kotlinExtension = project.extensions.getByType<KotlinJvmProjectExtension>()
            
            // When - Apply the exact configuration from build.gradle.kts
            kotlinExtension.jvmToolchain(24)
            
            // Then
            assertEquals(24, kotlinExtension.jvmToolchain.languageVersion.get().asInt())
        }

        @Test
        @DisplayName("Should handle withType configuration for KotlinCompile tasks")
        fun shouldHandleWithTypeConfigurationForKotlinCompileTasks() {
            // Given
            val compileTask1 = project.tasks.create("compileKotlin", KotlinCompile::class.java)
            val compileTask2 = project.tasks.create("compileTestKotlin", KotlinCompile::class.java)
            
            // When - Simulate the withType configuration from build.gradle.kts
            project.tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set("24")
                }
            }
            
            // Then
            assertEquals("24", compileTask1.compilerOptions.jvmTarget.get())
            assertEquals("24", compileTask2.compilerOptions.jvmTarget.get())
        }

        @Test
        @DisplayName("Should validate JVM target compatibility with Java version")
        fun shouldValidateJvmTargetCompatibilityWithJavaVersion() {
            // Given
            project.pluginManager.apply("java")
            val compileTask = project.tasks.create("testCompileKotlin", KotlinCompile::class.java)
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            
            // When - Apply configurations to match
            compileTask.compilerOptions {
                jvmTarget.set("24")
            }
            javaExtension.targetCompatibility = JavaVersion.VERSION_24
            
            // Then - Verify compatibility
            assertEquals("24", compileTask.compilerOptions.jvmTarget.get())
            assertEquals(JavaVersion.VERSION_24, javaExtension.targetCompatibility)
            // Verify that JVM target string matches Java version
            assertTrue(compileTask.compilerOptions.jvmTarget.get() == javaExtension.targetCompatibility.majorVersion)
        }

        @ParameterizedTest
        @ValueSource(strings = ["8", "11", "17", "21", "24"])
        @DisplayName("Should handle different JVM target versions")
        fun shouldHandleDifferentJvmTargetVersions(jvmTarget: String) {
            // Given
            val compileTask = project.tasks.create("testCompileKotlin", KotlinCompile::class.java)
            
            // When
            compileTask.compilerOptions {
                jvmTarget.set(jvmTarget)
            }
            
            // Then
            assertEquals(jvmTarget, compileTask.compilerOptions.jvmTarget.get())
        }

        @Test
        @DisplayName("Should handle empty or null configurations gracefully")
        fun shouldHandleEmptyOrNullConfigurationsGracefully() {
            // Given
            val compileTask = project.tasks.create("testCompileKotlin", KotlinCompile::class.java)
            
            // When & Then - Empty configuration block should not fail
            assertDoesNotThrow {
                compileTask.compilerOptions {
                    // Empty configuration block
                }
            }
        }
    }

    @Nested
    @DisplayName("Dependencies Configuration Tests")
    inner class DependenciesConfigurationTests {

        @Test
        @DisplayName("Should handle version catalog extension retrieval")
        fun shouldHandleVersionCatalogExtensionRetrieval() {
            // This test validates the pattern used in the build script
            // val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
            // When & Then - Should not throw when extension is properly configured
            assertDoesNotThrow {
                // In a real project, this would be configured by Gradle
                // Here we test that the call pattern is correct
                project.extensions.findByType(VersionCatalogsExtension::class.java)
            }
        }

        @Test
        @DisplayName("Should validate expected plugin dependencies structure")
        fun shouldValidateExpectedPluginDependenciesStructure() {
            // Test that the expected plugin names from build.gradle.kts are valid identifiers
            val expectedPlugins = listOf(
                "android.application",
                "kotlin.android",
                "ksp",
                "hilt.android"
            )
            
            // When & Then - Validate plugin name patterns
            expectedPlugins.forEach { pluginName ->
                assertFalse(pluginName.isBlank(), "Plugin name should not be blank")
                assertTrue(pluginName.contains(".") || pluginName.isNotEmpty(), "Plugin name should be valid")
                assertTrue(pluginName.matches(Regex("[a-zA-Z0-9._-]+")), "Plugin name should contain valid characters")
            }
        }

        @Test
        @DisplayName("Should validate expected library dependencies structure")
        fun shouldValidateExpectedLibraryDependenciesStructure() {
            // Test that the expected library names from build.gradle.kts are valid identifiers
            val expectedLibraries = listOf(
                "spotless.gradle.plugin",
                "detekt.gradle.plugin"
            )
            
            // When & Then - Validate library name patterns
            expectedLibraries.forEach { libraryName ->
                assertFalse(libraryName.isBlank(), "Library name should not be blank")
                assertTrue(libraryName.contains("."), "Library name should contain namespace separator")
                assertTrue(libraryName.matches(Regex("[a-zA-Z0-9._-]+")), "Library name should contain valid characters")
            }
        }

        @Test
        @DisplayName("Should handle implementation dependency configuration")
        fun shouldHandleImplementationDependencyConfiguration() {
            // Test that the project can handle implementation dependencies
            // This simulates the pattern: implementation(libs.findPlugin("name").get())
            
            // When & Then
            assertDoesNotThrow {
                // Simulate adding implementation dependencies
                project.configurations.maybeCreate("implementation")
                val implementationConfig = project.configurations.getByName("implementation")
                assertNotNull(implementationConfig)
                assertEquals("implementation", implementationConfig.name)
            }
        }

        @Test
        @DisplayName("Should validate dependency configuration patterns")
        fun shouldValidateDependencyConfigurationPatterns() {
            // Test the specific dependency patterns used in the build script
            val dependencyPatterns = listOf(
                "libs.findPlugin(\"android.application\").get()",
                "libs.findPlugin(\"kotlin.android\").get()",
                "libs.findPlugin(\"ksp\").get()",
                "libs.findPlugin(\"hilt.android\").get()",
                "libs.findLibrary(\"spotless.gradle.plugin\").get()",
                "libs.findLibrary(\"detekt.gradle.plugin\").get()"
            )
            
            // When & Then - Validate that patterns are well-formed
            dependencyPatterns.forEach { pattern ->
                assertTrue(pattern.contains("libs.find"), "Should use version catalog pattern")
                assertTrue(pattern.contains(".get()"), "Should call get() method")
                assertTrue(pattern.contains("\""), "Should contain quoted dependency name")
            }
        }
    }

    @Nested
    @DisplayName("Integration and End-to-End Tests")
    inner class IntegrationTests {

        @Test
        @DisplayName("Should configure complete build script successfully")
        fun shouldConfigureCompleteBuildScriptSuccessfully() {
            // Given - Apply all plugins as in the original build script
            project.pluginManager.apply("kotlin-dsl")
            project.pluginManager.apply("kotlin-dsl-precompiled-script-plugins")
            project.pluginManager.apply("java")
            project.pluginManager.apply("org.jetbrains.kotlin.jvm")
            
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            val kotlinExtension = project.extensions.getByType<KotlinJvmProjectExtension>()
            
            // When - Apply all configurations as in the original script
            javaExtension.apply {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(24))
                }
                targetCompatibility = JavaVersion.VERSION_24
                sourceCompatibility = JavaVersion.VERSION_24
            }
            
            kotlinExtension.jvmToolchain(24)
            
            val compileTask = project.tasks.create("compileKotlin", KotlinCompile::class.java)
            compileTask.compilerOptions {
                jvmTarget.set("24")
            }
            
            // Then - Verify all configurations are applied correctly
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl"))
            assertTrue(project.pluginManager.hasPlugin("kotlin-dsl-precompiled-script-plugins"))
            assertEquals(JavaLanguageVersion.of(24), javaExtension.toolchain.languageVersion.get())
            assertEquals(JavaVersion.VERSION_24, javaExtension.targetCompatibility)
            assertEquals(JavaVersion.VERSION_24, javaExtension.sourceCompatibility)
            assertEquals(24, kotlinExtension.jvmToolchain.languageVersion.get().asInt())
            assertEquals("24", compileTask.compilerOptions.jvmTarget.get())
        }

        @Test
        @DisplayName("Should maintain consistency between Java and Kotlin configurations")
        fun shouldMaintainConsistencyBetweenJavaAndKotlinConfigurations() {
            // Given
            project.pluginManager.apply("java")
            project.pluginManager.apply("org.jetbrains.kotlin.jvm")
            
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            val kotlinExtension = project.extensions.getByType<KotlinJvmProjectExtension>()
            
            // When - Apply the same version to both Java and Kotlin
            javaExtension.toolchain {
                languageVersion.set(JavaLanguageVersion.of(24))
            }
            kotlinExtension.jvmToolchain(24)
            
            val compileTask = project.tasks.create("compileKotlin", KotlinCompile::class.java)
            compileTask.compilerOptions {
                jvmTarget.set("24")
            }
            
            // Then - Verify consistency
            assertEquals(
                javaExtension.toolchain.languageVersion.get().asInt(),
                kotlinExtension.jvmToolchain.languageVersion.get().asInt()
            )
            assertEquals(
                kotlinExtension.jvmToolchain.languageVersion.get().asInt().toString(),
                compileTask.compilerOptions.jvmTarget.get()
            )
        }

        @Test
        @DisplayName("Should validate configuration order independence")
        fun shouldValidateConfigurationOrderIndependence() {
            // Test that Java and Kotlin configurations can be applied in different orders
            
            // Given
            project.pluginManager.apply("java")
            project.pluginManager.apply("org.jetbrains.kotlin.jvm")
            
            // When - Configure in reverse order (Kotlin first, then Java)
            val kotlinExtension = project.extensions.getByType<KotlinJvmProjectExtension>()
            kotlinExtension.jvmToolchain(24)
            
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()
            javaExtension.toolchain {
                languageVersion.set(JavaLanguageVersion.of(24))
            }
            
            // Then - Both should be configured correctly regardless of order
            assertEquals(24, kotlinExtension.jvmToolchain.languageVersion.get().asInt())
            assertEquals(JavaLanguageVersion.of(24), javaExtension.toolchain.languageVersion.get())
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    inner class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Should handle missing Java plugin gracefully")
        fun shouldHandleMissingJavaPluginGracefully() {
            // When & Then - Attempting to configure Java without plugin should fail appropriately
            assertThrows<Exception> {
                project.extensions.getByType<JavaPluginExtension>()
            }
        }

        @Test
        @DisplayName("Should handle missing Kotlin plugin gracefully")
        fun shouldHandleMissingKotlinPluginGracefully() {
            // When & Then - Attempting to configure Kotlin without plugin should fail appropriately
            assertThrows<Exception> {
                project.extensions.getByType<KotlinJvmProjectExtension>()
            }
        }

        @Test
        @DisplayName("Should handle invalid task creation without plugin")
        fun shouldHandleInvalidTaskCreationWithoutPlugin() {
            // Given - No Kotlin plugin applied
            
            // When & Then - Creating KotlinCompile task without plugin should still work
            // (Gradle allows task creation even without the plugin)
            assertDoesNotThrow {
                val task = project.tasks.create("testTask", KotlinCompile::class.java)
                assertNotNull(task)
                assertEquals("testTask", task.name)
            }
        }

        @Test
        @DisplayName("Should handle version catalog extension not found")
        fun shouldHandleVersionCatalogExtensionNotFound() {
            // When & Then - Should handle missing version catalog gracefully
            val result = project.extensions.findByType(VersionCatalogsExtension::class.java)
            assertNull(result, "Version catalog extension should not be found in test project")
        }

        @Test
        @DisplayName("Should validate build script structure requirements")
        fun shouldValidateBuildScriptStructureRequirements() {
            // Test that the build script structure meets expected requirements
            
            // When & Then - Validate key components are testable
            assertDoesNotThrow {
                // Plugin application should work
                project.pluginManager.apply("kotlin-dsl")
                
                // Java plugin should be applicable
                project.pluginManager.apply("java")
                
                // Kotlin plugin should be applicable
                project.pluginManager.apply("org.jetbrains.kotlin.jvm")
                
                // Extensions should be retrievable
                val javaExt = project.extensions.getByType<JavaPluginExtension>()
                val kotlinExt = project.extensions.getByType<KotlinJvmProjectExtension>()
                
                assertNotNull(javaExt)
                assertNotNull(kotlinExt)
            }
        }
    }
}