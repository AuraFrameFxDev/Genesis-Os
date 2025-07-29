# PowerShell script to run Gradle with Java 24

# Set Java home to Java 24
$javaHome = "C:\Program Files\Java\jdk-24.0.2"
$env:JAVA_HOME = $javaHome

# Add Java 24 bin to PATH
$env:Path = "$javaHome\bin;" + $env:Path

# Verify Java version
Write-Host "Using Java version:"
java -version

# Run Gradle with the specified arguments
$gradleArgs = $args -join " "
Write-Host "Running: .\gradlew $gradleArgs"
.\gradlew $gradleArgs
