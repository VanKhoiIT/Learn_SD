@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist "bin" mkdir "bin"

echo ================================================================================
echo                     BUILD AND RUN AUTOMATION: LAB                            
echo         BiCardSource Unit Testing with Stubs and 100%% Branch Coverage          
echo ================================================================================
echo.
echo [1/3] Compiling Java source files with --release 21...
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
echo [2/3] Running Automated Unit Tests via TestRunner (Java Reflection)...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.TestRunner
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Unit tests failed!
    pause
    exit /b 1
)
echo.
echo [3/3] Running Branch Coverage Analyzer Demo...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.CoverageAnalyzerDemo
echo.
echo ================================================================================
echo                        ALL PROCESSES COMPLETED SUCCESSFULLY                     
echo ================================================================================
pause
