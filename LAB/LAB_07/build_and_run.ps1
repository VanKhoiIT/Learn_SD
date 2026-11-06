[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "                     BUILD AND RUN AUTOMATION: LAB_07                           " -ForegroundColor Cyan
Write-Host "          Unpacking Composite Shows: Safety (Ex 6.6) vs Transparency (Ex 6.7)   " -ForegroundColor Cyan
Write-Host "================================================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "`n[1/5] Compiling Java source and test files with --release 21..." -ForegroundColor Yellow
$sources = Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
& javac --release 21 -encoding UTF-8 -cp "lib/*" -d "bin" $sources
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "[OK] Compilation successful!" -ForegroundColor Green

Write-Host "`n[2/5] Running Automated Unit Tests via TestRunner (Java Reflection)..." -ForegroundColor Yellow
& java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions6.TestRunner
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Unit tests failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`n[3/5] Running Exercise 06 Demo (Safety Approach - Package exercise06)..." -ForegroundColor Yellow
& java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions6.exercise06.Exercise06

Write-Host "`n[4/5] Running Exercise 07 Demo (Transparency Approach - Package exercise07)..." -ForegroundColor Yellow
& java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions6.exercise07.Exercise07

Write-Host "`n[5/5] Running Side-by-Side Architectural Comparison (ComparisonDemo)..." -ForegroundColor Yellow
& java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions6.ComparisonDemo

# Optional: Generate DOCX report if Python is present
if (Get-Command python -ErrorAction SilentlyContinue) {
    Write-Host "`n[BONUS] Generating Word Academic Report (BAO_CAO_LAB_07_EXERCISE_6_7.docx)..." -ForegroundColor Yellow
    & python generate_docx.py
}

Write-Host "`n================================================================================" -ForegroundColor Green
Write-Host "                        ALL PROCESSES COMPLETED SUCCESSFULLY                     " -ForegroundColor Green
Write-Host "================================================================================" -ForegroundColor Green
