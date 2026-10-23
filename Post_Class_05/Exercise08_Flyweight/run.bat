@echo off
chcp 65001 > nul
echo Dang bien dich ma nguon Java (Exercise 08 - Flyweight)...
javac -encoding UTF-8 -d . *.java
if %errorlevel% neq 0 (
    echo [LOI] Bien dich that bai!
    pause
    exit /b %errorlevel%
)
echo Bien dich thanh cong!
echo.
echo =======================================================
echo 1. CHAY KIEM THU CO BAN (Exercise08)
echo =======================================================
java -Dfile.encoding=UTF-8 exercise08.Exercise08
echo.
echo =======================================================
echo 2. CHAY SO SANH HIEN NANG CHI TIET (Exercise08Comparison)
echo =======================================================
java -Dfile.encoding=UTF-8 exercise08.Exercise08Comparison
echo.
pause
