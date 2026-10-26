@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion
title Post Class 06 - Unit Testing & Coverage (Exercises 5.8, 5.9, 5.10)
cd /d "%~dp0"

where javac >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] JDK 'javac' not found in PATH!
    pause
    exit /b 1
)

if not exist "bin" mkdir "bin"

echo ================================================================
echo   COMPILING POST CLASS 06 (DesignBook Exercises 5.8, 5.9, 5.10)
echo ================================================================

set "SOURCES="
for /r "src" %%f in (*.java) do (
    set "SOURCES=!SOURCES! "%%f""
)

javac --release 21 -encoding UTF-8 -cp "lib/*" -d "bin" !SOURCES!
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [OK] Compilation successful! Classes (Java 21 bytecode) stored in 'bin/'.
echo.

:MENU
echo ================================================================
echo                         SELECT AN ACTION
echo ================================================================
echo 1. Run Automated Unit Tests (TestRunner - TestPokerHand)
echo 2. Run Comprehensive Coverage Analyzer Demo (Detailed Analysis)
echo 3. Re-generate Word Academic Report (.docx)
echo 4. Exit
echo ================================================================
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" (
    echo.
    java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.TestRunner
    echo.
    goto MENU
)

if "%choice%"=="2" (
    echo.
    java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.CoverageAnalyzerDemo
    echo.
    goto MENU
)

if "%choice%"=="3" (
    echo.
    echo Generating Word report...
    python generate_docx.py
    echo.
    goto MENU
)

if "%choice%"=="4" (
    echo Goodbye!
    exit /b 0
)

echo Invalid choice, please try again.
echo.
goto MENU
