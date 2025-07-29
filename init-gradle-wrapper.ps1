# Create a temporary build.gradle file to initialize the wrapper
@"
plugins {
    id 'base'
}

task initWrapper(type: Wrapper) {
    gradleVersion = '8.1.4'
    distributionType = Wrapper.DistributionType.ALL
}
"@ | Out-File -FilePath "$PSScriptRoot\init.gradle" -Encoding ASCII

# Download Gradle if not present
$gradleVersion = "8.1.4"
$gradleHome = "$env:USERPROFILE\.gradle\wrapper\dists\gradle-$gradleVersion-bin"
$gradleZip = "gradle-$gradleVersion-bin.zip"
$gradleUrl = "https://services.gradle.org/distributions/$gradleZip"

# Check if Gradle is already downloaded
if (-not (Test-Path -Path $gradleHome)) {
    Write-Host "Downloading Gradle $gradleVersion..."
    $tempDir = [System.IO.Path]::GetTempPath()
    $tempZip = Join-Path $tempDir $gradleZip
    
    try {
        Invoke-WebRequest -Uri $gradleUrl -OutFile $tempZip
        
        # Create the target directory
        New-Item -ItemType Directory -Path $gradleHome -Force | Out-Null
        
        # Extract the zip file
        Expand-Archive -Path $tempZip -DestinationPath $gradleHome -Force
        
        # Clean up
        Remove-Item -Path $tempZip -Force
    }
    catch {
        Write-Host "Failed to download Gradle: $_"
        exit 1
    }
}

# Find the extracted Gradle directory
$gradleDir = Get-ChildItem -Path $gradleHome -Directory | Select-Object -First 1
if (-not $gradleDir) {
    Write-Host "Failed to find extracted Gradle distribution"
    exit 1
}

# Set up the Gradle wrapper
$gradleCmd = Join-Path $gradleDir.FullName "gradle-$gradleVersion\bin\gradle.bat"
if (Test-Path $gradleCmd) {
    Write-Host "Setting up Gradle wrapper..."
    & $gradleCmd -p $PSScriptRoot -b init.gradle initWrapper --no-daemon
    
    # Clean up
    Remove-Item -Path "$PSScriptRoot\init.gradle" -Force
    
    Write-Host "Gradle wrapper setup complete. You can now use 'gradlew.bat' to build the project."
} else {
    Write-Host "Gradle command not found at: $gradleCmd"
    exit 1
}
