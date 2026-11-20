@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist "bin" mkdir "bin"

echo ================================================================================
echo                     BUILD AND RUN AUTOMATION: LAB_09                           
echo     Template Method Pattern: E-Commerce Order Processing Pipeline              
echo ================================================================================

echo.
echo [1/3] Compiling Java source and test files with --release 21...
setlocal enabledelayedexpansion
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
echo [OK] Compilation successful!

echo.
echo [2/3] Running Automated Unit Tests (TestRunner via Reflection)...
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "bin;lib/*" lab09.orderprocessing.TestRunner
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Unit tests failed!
    pause
    exit /b 1
)

echo.
echo [3/3] Running Main Demo (E-Commerce Multi-Channel Order Processing Simulation)...
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "bin;lib/*" lab09.orderprocessing.MainDemo

echo.
echo ================================================================================
echo                     ALL STEPS COMPLETED SUCCESSFULLY!                          
echo ================================================================================
pause

