@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist "bin" mkdir "bin"

echo Compiling Java source files...
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
echo Running TestRunner...
echo.
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.TestRunner
pause
