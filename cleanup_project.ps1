# Script to clean up unnecessary files and folders
# Run this from the project root directory

# Remove build directories
Remove-Item -Path ".\.gradle" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "\.gradle-local" -Recurse -Force -ErrorAction SilentlyContinue

# Remove duplicate module (keeping collab-canvas)
Remove-Item -Path ".\colleb-canvas" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".\oracledrive-integration" -Recurse -Force -ErrorAction SilentlyContinue

# Remove temporary scripts
$scriptsToRemove = @(
    "disable_ios_arm32.ps1",
    "fix_ios_arm32.gradle",
    "update_gradle_wrapper.ps1",
    "setup-gradle-wrapper.ps1",
    "setup-gradle.ps1",
    "setup-java24.ps1",
    "setup-local-gradle.ps1",
    "setup-openjdk24.ps1",
    "setup_gradle_wrapper.ps1",
    "gradlew-local.ps1",
    "run_with_java24.bat",
    "run_with_java24.ps1",
    "gradlew-java24.bat"
)

foreach ($script in $scriptsToRemove) {
    Remove-Item -Path $script -Force -ErrorAction SilentlyContinue
}

# Clean up Gradle files
Remove-Item -Path "gradle-*.zip" -Force -ErrorAction SilentlyContinue

Write-Host "Cleanup complete. Project directory has been cleaned."
