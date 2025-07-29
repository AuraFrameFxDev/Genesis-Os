# Create necessary directories
$gradleDir = "$PSScriptRoot\gradle\wrapper"
if (-not (Test-Path -Path $gradleDir)) {
    New-Item -ItemType Directory -Path $gradleDir -Force | Out-Null
}

# Download gradle-wrapper.jar from a reliable source
$wrapperUrl = "https://github.com/gradle/gradle/raw/v8.0.2/gradle/wrapper/gradle-wrapper.jar"
$wrapperPath = "$gradleDir\gradle-wrapper.jar"

Write-Host "Downloading gradle-wrapper.jar..."
try {
    # Use TLS 1.2 for the download
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperPath -UseBasicParsing
    Write-Host "Download completed successfully."
} catch {
    Write-Host "Failed to download gradle-wrapper.jar: $_"
    exit 1
}

# Verify the download
if (-not (Test-Path -Path $wrapperPath)) {
    Write-Host "gradle-wrapper.jar was not downloaded successfully."
    exit 1
}

# Create gradle-wrapper.properties with correct settings
@"
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0.2-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
"@ | Out-File -FilePath "$gradleDir\gradle-wrapper.properties" -Encoding ASCII

Write-Host "Gradle wrapper setup complete."
Write-Host "You can now run './gradlew' (on Unix) or 'gradlew.bat' (on Windows)."
