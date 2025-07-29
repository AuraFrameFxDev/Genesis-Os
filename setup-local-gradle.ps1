# Create a local gradle directory
$localGradleDir = "$PSScriptRoot\.gradle-local"
if (-not (Test-Path -Path $localGradleDir)) {
    New-Item -ItemType Directory -Path $localGradleDir | Out-Null
}

# Download Gradle distribution
$gradleVersion = "8.14.3"
$gradleZip = "gradle-$gradleVersion-bin.zip"
$gradleUrl = "https://services.gradle.org/distributions/$gradleZip"
$gradleZipPath = "$localGradleDir\$gradleZip"
$gradleHome = "$localGradleDir\gradle-$gradleVersion"

# Download Gradle if not already present
if (-not (Test-Path -Path $gradleHome)) {
    Write-Host "Downloading Gradle $gradleVersion..."
    Invoke-WebRequest -Uri $gradleUrl -OutFile $gradleZipPath
    
    # Extract Gradle
    Write-Host "Extracting Gradle..."
    Expand-Archive -Path $gradleZipPath -DestinationPath $localGradleDir -Force
    Remove-Item -Path $gradleZipPath
}

# Create a gradlew.bat replacement
@"
@echo off
setlocal

set GRADLE_HOME=%~dp0.gradle-local\gradle-$gradleVersion
set PATH=%GRADLE_HOME%\bin;%PATH%

gradlew.bat %*
"@ | Out-File -FilePath "$PSScriptRoot\gradlew-local.bat" -Encoding ASCII

Write-Host "Local Gradle setup complete. Use 'gradlew-local.bat' instead of 'gradlew.bat'"
Write-Host "Example: .\gradlew-local.bat build"
