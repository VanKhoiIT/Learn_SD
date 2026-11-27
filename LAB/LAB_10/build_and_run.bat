@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist "bin" mkdir "bin"

echo ================================================================================
echo                     BUILD AND RUN AUTOMATION: LAB_10                            
echo      Observer Pattern: Push vs Pull Data-Flow ^& Interface Segregation (8.6-8.7) 
echo ================================================================================

echo.
echo [1/4] Compiling Java source and test files with --release 21...
setlocal enabledelayedexpansion
set "SOURCES="
for /r "src" %%f in (*.java) do (
    set "SOURCES=!SOURCES! "%%f""
)

javac --release 21 -encoding UTF-8 -cp "lib/*" -d "bin" !SOURCES!
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed!
    exit /b 1
)
echo [OK] Compilation successful!

echo.
echo [2/4] Running Automated Unit Tests (TestRunner)...
java -ea -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions8.TestRunner
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Unit tests failed!
    exit /b 1
)

echo.
echo [3/4] Running Exercise 8.6 Demo (Push Data-Flow ^& Single Observer Interface)...
java -ea -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions8.exercise06.Exercise06

echo.
echo [4/4] Running Exercise 8.7 Demo (Pull Data-Flow ^& Segregated Interfaces with ISP)...
java -ea -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions8.exercise07.Exercise07

echo.
echo ================================================================================
echo                        ALL PROCESSES COMPLETED SUCCESSFULLY                     
echo ================================================================================
