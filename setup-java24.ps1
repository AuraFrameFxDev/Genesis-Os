# Create a local Java directory
$javaDir = "$PSScriptRoot\.java"
if (-not (Test-Path -Path $javaDir)) {
    New-Item -ItemType Directory -Path $javaDir | Out-Null
}

# Download Zulu JDK 24
$javaVersion = "24.0.2"
$javaUrl = "https://cdn.azul.com/zulu/bin/zulu24.32.17-ca-jdk24.0.2-win_x64.zip"
$javaZip = "$javaDir\zulu-jdk24.zip"
$javaHome = "$javaDir\zulu24.0.2"

# Download Zulu JDK if not already present
if (-not (Test-Path -Path $javaHome)) {
    Write-Host "Downloading Zulu JDK 24..."
    Invoke-WebRequest -Uri $javaUrl -OutFile $javaZip
    
    # Extract JDK
    Write-Host "Extracting Zulu JDK..."
    Expand-Archive -Path $javaZip -DestinationPath $javaDir -Force
    Remove-Item -Path $javaZip
    
    # Rename the extracted directory to a consistent name
    $extractedDir = Get-ChildItem -Path $javaDir -Directory | Where-Object { $_.Name -like "zulu*" } | Select-Object -First 1
    if ($extractedDir) {
        Rename-Item -Path $extractedDir.FullName -NewName (Split-Path -Leaf $javaHome) -Force
    }
}

# Create a batch file to set up the environment
@"
@echo off
setlocal

:: Set Java home
set JAVA_HOME=%~dp0.java\zulu24.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

:: Run the original gradlew.bat with the new Java
call "%~dp0gradlew.bat" %*
"@ | Out-File -FilePath "$PSScriptRoot\gradlew-java24.bat" -Encoding ASCII

Write-Host "Zulu JDK 24 setup complete. Use 'gradlew-java24.bat' instead of 'gradlew.bat'"
Write-Host "Example: .\gradlew-java24.bat build"

# Show current Java version
Write-Host "`nCurrent Java version:"
& "$javaHome\bin\java" -version
