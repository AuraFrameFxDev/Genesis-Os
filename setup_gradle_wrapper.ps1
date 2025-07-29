# PowerShell script to set up Gradle wrapper

# Set Java home to Java 24
$javaHome = "C:\Program Files\Java\jdk-24.0.2"
$env:JAVA_HOME = $javaHome
$env:Path = "$javaHome\bin;" + $env:Path

# Download Gradle wrapper JAR
$gradleVersion = "8.14.3"
$gradleWrapperJarUrl = "https://services.gradle.org/distributions/gradle-$($gradleVersion)-bin.zip"
$tempDir = [System.IO.Path]::GetTempPath()
$gradleZip = Join-Path -Path $tempDir -ChildPath "gradle-$gradleVersion-bin.zip"
$gradleDir = Join-Path -Path $tempDir -ChildPath "gradle-$gradleVersion"

Write-Host "Downloading Gradle $gradleVersion..."
Invoke-WebRequest -Uri $gradleWrapperJarUrl -OutFile $gradleZip

# Extract Gradle
Write-Host "Extracting Gradle..."
Expand-Archive -Path $gradleZip -DestinationPath $tempDir -Force

# Set up Gradle wrapper
Write-Host "Setting up Gradle wrapper..."
& "$gradleDir\gradle-$gradleVersion\bin\gradle" wrapper --gradle-version $gradleVersion --distribution-type bin

# Verify Gradle wrapper
Write-Host "Verifying Gradle wrapper..."
.\gradlew --version

Write-Host "Gradle wrapper setup complete!"
