# PowerShell Build and Run Script for Post_Class_06
$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

Write-Host "================================================================" -ForegroundColor Cyan
Write-Host "   BUILDING POST CLASS 06 (DesignBook Exercises 5.8, 5.9, 5.10) " -ForegroundColor Cyan
Write-Host "================================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

$sources = (Get-ChildItem -Path "src" -Recurse -Filter "*.java").FullName
javac --release 21 -encoding UTF-8 -cp "lib/*" -d "bin" $sources

Write-Host "[OK] Compilation Successful! Output (Java 21 bytecode) in 'bin/' folder.`n" -ForegroundColor Green

Write-Host "Executing Automated Unit Tests..." -ForegroundColor Yellow
java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions5.TestRunner

Write-Host "`nExecuting Comprehensive Coverage Analysis Demo..." -ForegroundColor Yellow
java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions5.CoverageAnalyzerDemo
