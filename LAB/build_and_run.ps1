[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Stop"

Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "                     BUILD AND RUN AUTOMATION: LAB                            " -ForegroundColor Cyan
Write-Host "         BiCardSource Unit Testing with Stubs and 100% Branch Coverage          " -ForegroundColor Cyan
Write-Host "================================================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "`n[1/3] Compiling Java source files with --release 21..." -ForegroundColor Yellow
$sources = Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
& javac --release 21 -encoding UTF-8 -cp "lib/*" -d "bin" $sources
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "[OK] Compilation successful!" -ForegroundColor Green

Write-Host "`n[2/3] Running Automated Unit Tests via TestRunner (Java Reflection)..." -ForegroundColor Yellow
& java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions5.TestRunner
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Unit tests failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`n[3/3] Running Branch Coverage Analyzer Demo..." -ForegroundColor Yellow
& java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" designbook.solutions5.CoverageAnalyzerDemo

Write-Host "`n================================================================================" -ForegroundColor Green
Write-Host "                        ALL PROCESSES COMPLETED SUCCESSFULLY                     " -ForegroundColor Green
Write-Host "================================================================================" -ForegroundColor Green
