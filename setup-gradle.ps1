# Create gradle/wrapper directory if it doesn't exist
$gradleWrapperDir = "$PSScriptRoot\gradle\wrapper"
if (-not (Test-Path -Path $gradleWrapperDir)) {
    New-Item -ItemType Directory -Path $gradleWrapperDir -Force | Out-Null
}

# Download gradle-wrapper.jar
$wrapperJarUrl = "https://raw.githubusercontent.com/gradle/gradle/v8.14.3/gradle/wrapper/gradle-wrapper.jar"
$wrapperJarPath = "$gradleWrapperDir\gradle-wrapper.jar"
Invoke-WebRequest -Uri $wrapperJarUrl -OutFile $wrapperJarPath

# Create gradle-wrapper.properties
$wrapperPropertiesPath = "$gradleWrapperDir\gradle-wrapper.properties"
@"
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14.3-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
"@ | Out-File -FilePath $wrapperPropertiesPath -Encoding ASCII

Write-Host "Gradle wrapper setup complete. You can now use .\gradlew.bat to run Gradle commands."
