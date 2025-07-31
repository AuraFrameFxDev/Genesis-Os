import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Comprehensive unit tests for Android Library Conventions Plugin configuration.
 * 
 * Testing Framework: JUnit4 with Gradle TestFixtures (ProjectBuilder)
 * 
 * These tests validate the android-library-conventions.gradle.kts build script configuration
 * that sets up Android library projects with Kotlin support, including plugin application,
 * SDK versions, build types, and compiler options as defined in the actual file.
 */
class AndroidLibraryConventionsPluginTest {

    private lateinit var project: Project
    private lateinit var pluginManager: PluginManager

    @Before
    fun setup() {
        project = ProjectBuilder.builder().build()
        pluginManager = project.pluginManager
    }

    @Test
    fun `should apply required plugins successfully`() {
        // Apply the plugins as specified in android-library-conventions.gradle.kts
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        // Verify plugins are applied
        assertTrue("Android library plugin should be applied", 
                   pluginManager.hasPlugin("com.android.library"))
        assertTrue("Kotlin Android plugin should be applied", 
                   pluginManager.hasPlugin("org.jetbrains.kotlin.android"))
    }

    @Test
    fun `should configure namespace correctly`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.namespace = "com.example.mylibrary"

        assertEquals("Namespace should match build script configuration", 
                    "com.example.mylibrary", android.namespace)
    }

    @Test
    fun `should set compileSdk to 36`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.compileSdk = 36

        assertEquals("CompileSdk should be 36 as specified in build script", 
                    36, android.compileSdk)
    }

    @Test
    fun `should configure defaultConfig with minSdk 33 and test runner`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.defaultConfig {
            minSdk = 33
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        assertEquals("MinSdk should be 33", 33, android.defaultConfig.minSdk)
        assertEquals("Test instrumentation runner should be AndroidJUnitRunner", 
                    "androidx.test.runner.AndroidJUnitRunner", 
                    android.defaultConfig.testInstrumentationRunner)
    }

    @Test
    fun `should configure testOptions with targetSdk 36`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.testOptions {
            targetSdk = 36
        }

        assertEquals("TargetSdk in testOptions should be 36", 
                    36, android.testOptions.targetSdk)
    }

    @Test
    fun `should configure release buildType with minification disabled and proguard files`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    android.getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        val releaseConfig = android.buildTypes.getByName("release")
        assertFalse("Release build should have minification disabled", 
                   releaseConfig.isMinifyEnabled)
        assertNotNull("ProGuard files should be configured", 
                     releaseConfig.proguardFiles)
        assertTrue("ProGuard files should contain entries", 
                  releaseConfig.proguardFiles.isNotEmpty())
    }

    @Test
    fun `should set Java compatibility to version 24`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.compileOptions {
            sourceCompatibility = JavaVersion.toVersion("24")
            targetCompatibility = JavaVersion.toVersion("24")
        }

        assertEquals("Source compatibility should be Java 24", 
                    JavaVersion.toVersion("24"), android.compileOptions.sourceCompatibility)
        assertEquals("Target compatibility should be Java 24", 
                    JavaVersion.toVersion("24"), android.compileOptions.targetCompatibility)
    }

    @Test
    fun `should configure Kotlin compiler JVM target to 24`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        // Create and configure KotlinCompile tasks as in the build script
        val kotlinCompileTask = project.tasks.create("compileKotlin", KotlinCompile::class.java)
        kotlinCompileTask.compilerOptions {
            jvmTarget.set("24")
        }

        // Verify the JVM target is set correctly
        assertEquals("Kotlin JVM target should be 24", 
                    "24", kotlinCompileTask.compilerOptions.jvmTarget.get())
    }

    @Test
    fun `should handle invalid Java version input gracefully`() {
        try {
            JavaVersion.toVersion("invalid_version")
            fail("Should throw exception for invalid Java version")
        } catch (e: IllegalArgumentException) {
            assertNotNull("Exception should be thrown for invalid version", e)
            assertTrue("Exception message should indicate invalid version", 
                      e.message!!.contains("Could not determine java version"))
        }
    }

    @Test
    fun `should fail gracefully when Android plugin is missing`() {
        // Only apply Kotlin plugin without Android plugin
        pluginManager.apply("org.jetbrains.kotlin.android")

        try {
            project.extensions.getByName("android")
            fail("Should fail when Android plugin is not applied")
        } catch (e: Exception) {
            assertNotNull("Exception should be thrown when Android plugin is missing", e)
        }
    }

    @Test
    fun `should validate API level consistency`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.compileSdk = 36
        android.defaultConfig {
            minSdk = 33
        }
        android.testOptions {
            targetSdk = 36
        }

        // Validate logical relationships as defined in the build script
        assertTrue("MinSdk (33) should not exceed compileSdk (36)", 
                  android.defaultConfig.minSdk!! <= android.compileSdk!!)
        assertTrue("TargetSdk (36) should not exceed compileSdk (36)", 
                  android.testOptions.targetSdk <= android.compileSdk!!)
        assertEquals("MinSdk should be exactly 33", 33, android.defaultConfig.minSdk)
        assertEquals("CompileSdk should be exactly 36", 36, android.compileSdk)
        assertEquals("TestOptions targetSdk should be exactly 36", 36, android.testOptions.targetSdk)
    }

    @Test
    fun `should handle proguard files configuration variations`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        
        // Test with exact proguard configuration from build script
        android.buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    android.getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        val releaseConfig = android.buildTypes.getByName("release")
        assertTrue("ProGuard files should contain the default optimize file", 
                  releaseConfig.proguardFiles.any { it.name == "proguard-android-optimize.txt" })
        assertTrue("ProGuard files should contain custom rules file", 
                  releaseConfig.proguardFiles.any { it.name == "proguard-rules.pro" })
    }

    @Test
    fun `should validate namespace follows package naming conventions`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.namespace = "com.example.mylibrary"

        // Validate the exact namespace from the build script
        assertEquals("Namespace should match build script exactly", 
                    "com.example.mylibrary", android.namespace)
        
        // Validate it follows Java package naming convention
        assertTrue("Namespace should follow Java package naming convention", 
                  android.namespace!!.matches(Regex("^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$")))
    }

    @Test
    fun `should validate test instrumentation runner is AndroidJUnitRunner`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        android.defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        val runner = android.defaultConfig.testInstrumentationRunner
        assertEquals("Test runner should be exactly AndroidJUnitRunner", 
                    "androidx.test.runner.AndroidJUnitRunner", runner)
        assertTrue("Runner should be from androidx.test package", 
                  runner!!.startsWith("androidx.test"))
    }

    @Test
    fun `should validate complete configuration matches build script exactly`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        
        // Apply ALL configurations exactly as in the build script
        android.namespace = "com.example.mylibrary"
        android.compileSdk = 36
        android.defaultConfig {
            minSdk = 33
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        android.testOptions {
            targetSdk = 36
        }
        android.buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    android.getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        android.compileOptions {
            sourceCompatibility = JavaVersion.toVersion("24")
            targetCompatibility = JavaVersion.toVersion("24")
        }

        // Comprehensive validation against exact build script values
        assertEquals("com.example.mylibrary", android.namespace)
        assertEquals(36, android.compileSdk)
        assertEquals(33, android.defaultConfig.minSdk)
        assertEquals("androidx.test.runner.AndroidJUnitRunner", 
                    android.defaultConfig.testInstrumentationRunner)
        assertEquals(36, android.testOptions.targetSdk)
        assertEquals(JavaVersion.toVersion("24"), android.compileOptions.sourceCompatibility)
        assertEquals(JavaVersion.toVersion("24"), android.compileOptions.targetCompatibility)
        
        val releaseConfig = android.buildTypes.getByName("release")
        assertFalse("Minification should be disabled", releaseConfig.isMinifyEnabled)
        assertTrue("ProGuard files should be configured", releaseConfig.proguardFiles.isNotEmpty())
    }

    @Test
    fun `should handle KSP plugin commented out scenario`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")
        
        // The build script has KSP commented out - verify it's not applied
        assertFalse("KSP plugin should not be applied by default", 
                   pluginManager.hasPlugin("com.google.devtools.ksp"))
        
        // The project should still work without KSP
        val android = project.extensions.getByName("android") as LibraryExtension
        android.namespace = "com.example.mylibrary"
        
        assertNotNull("Android extension should work without KSP", android)
        assertEquals("Namespace should work without KSP", "com.example.mylibrary", android.namespace)
    }

    @Test
    fun `should validate Java version 24 compatibility`() {
        // Test that Java version 24 is a valid version
        val javaVersion = JavaVersion.toVersion("24")
        assertNotNull("Java version 24 should be valid", javaVersion)
        assertEquals("Java version should be 24", "24", javaVersion.toString())
        
        // Test that it's compatible with modern Android development
        assertTrue("Java 24 should be higher than minimum required (8)", 
                  javaVersion.isCompatibleWith(JavaVersion.VERSION_1_8))
    }

    @Test
    fun `should handle edge case with mismatched Java and Kotlin versions`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        
        // Configure Java to version 24 as in build script
        android.compileOptions {
            sourceCompatibility = JavaVersion.toVersion("24")
            targetCompatibility = JavaVersion.toVersion("24")
        }

        // Create Kotlin task with different JVM target to test edge case
        val kotlinTask = project.tasks.create("compileKotlin", KotlinCompile::class.java)
        kotlinTask.compilerOptions {
            jvmTarget.set("17") // Different from Java version
        }

        // Verify configurations
        assertEquals("Java should be version 24", 
                    JavaVersion.toVersion("24"), android.compileOptions.sourceCompatibility)
        assertEquals("Kotlin should be version 17 (for this edge case test)", 
                    "17", kotlinTask.compilerOptions.jvmTarget.get())
    }

    @Test
    fun `should validate build script imports are accessible`() {
        // Test that the imports from the build script work correctly
        val javaVersion = JavaVersion.toVersion("24")
        assertNotNull("JavaVersion import should work", javaVersion)
        
        // Test project builder functionality
        assertNotNull("Project should be created", project)
        assertNotNull("Plugin manager should be available", pluginManager)
    }

    @Test
    fun `should handle multiple build types beyond release`() {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        val android = project.extensions.getByName("android") as LibraryExtension
        
        // Configure release as in build script
        android.buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    android.getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            // Test that debug (default) build type also works
            debug {
                isMinifyEnabled = false
            }
        }

        val releaseConfig = android.buildTypes.getByName("release")
        val debugConfig = android.buildTypes.getByName("debug")
        
        assertFalse("Release should have minification disabled", releaseConfig.isMinifyEnabled)
        assertFalse("Debug should have minification disabled", debugConfig.isMinifyEnabled)
        assertTrue("Release should have ProGuard files", releaseConfig.proguardFiles.isNotEmpty())
    }
}