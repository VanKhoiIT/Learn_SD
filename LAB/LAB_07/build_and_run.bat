@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist "bin" mkdir "bin"

echo ================================================================================
echo                     BUILD AND RUN AUTOMATION: LAB_07                           
echo          Unpacking Composite Shows: Safety (Ex 6.6) vs Transparency (Ex 6.7)   
echo ================================================================================

echo.
echo [1/5] Compiling Java source and test files with --release 21...
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
echo [2/5] Running Automated Unit Tests (TestRunner via Reflection)...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions6.TestRunner
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Unit tests failed!
    pause
    exit /b 1
)

echo.
echo [3/5] Running Exercise 06 Demo (Safety Approach - Package exercise06)...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions6.exercise06.Exercise06

echo.
echo [4/5] Running Exercise 07 Demo (Transparency Approach - Package exercise07)...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions6.exercise07.Exercise07

echo.
echo [5/5] Running Side-by-Side Architectural Comparison (ComparisonDemo)...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions6.ComparisonDemo

where python >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo.
    echo [BONUS] Generating Word Academic Report: BAO_CAO_LAB_07_EXERCISE_6_7.docx ...
    python generate_docx.py
)

echo.
echo ================================================================================
echo                        ALL PROCESSES COMPLETED SUCCESSFULLY                     
echo ================================================================================
pause
