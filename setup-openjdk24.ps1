# Create a local Java directory
$javaDir = "$PSScriptRoot\.java"
if (-not (Test-Path -Path $javaDir)) {
    New-Item -ItemType Directory -Path $javaDir | Out-Null
}

# Download OpenJDK 24
$javaVersion = "24.0.2"
$javaUrl = "https://download.java.net/java/GA/jdk24.0.2/fdc5d0102fe0414db21410ad5834341f/12/GPL/openjdk-24.0.2_windows-x64_bin.zip"
$javaZip = "$javaDir\openjdk-24.0.2_windows-x64_bin.zip"
$javaHome = "$javaDir\jdk-24.0.2"

# Download OpenJDK if not already present
if (-not (Test-Path -Path $javaHome)) {
    Write-Host "Downloading OpenJDK 24..."
    Invoke-WebRequest -Uri $javaUrl -OutFile $javaZip
    
    # Extract JDK
    Write-Host "Extracting OpenJDK..."
    Expand-Archive -Path $javaZip -DestinationPath $javaDir -Force
    Remove-Item -Path $javaZip
    
    # Rename the extracted directory to a consistent name
    $extractedDir = Get-ChildItem -Path $javaDir -Directory | Where-Object { $_.Name -like "jdk-24*" } | Select-Object -First 1
    if ($extractedDir) {
        Rename-Item -Path $extractedDir.FullName -NewName (Split-Path -Leaf $javaHome) -Force
    }
}

# Create a batch file to set up the environment
@"
@echo off
setlocal

:: Set Java home
set JAVA_HOME=%~dp0.java\jdk-24.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

:: Run the original gradlew.bat with the new Java
call "%~dp0gradlew.bat" %*
"@ | Out-File -FilePath "$PSScriptRoot\gradlew-java24.bat" -Encoding ASCII

Write-Host "OpenJDK 24 setup complete. Use 'gradlew-java24.bat' instead of 'gradlew.bat'"
Write-Host "Example: .\gradlew-java24.bat build"

# Show current Java version
Write-Host "`nCurrent Java version:"
& "$javaHome\bin\java" -version
