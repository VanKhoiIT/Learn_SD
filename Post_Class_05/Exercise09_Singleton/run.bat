@echo off
chcp 65001 > nul
echo Dang bien dich ma nguon Java...
javac -encoding UTF-8 -d . *.java
if %errorlevel% neq 0 (
    echo [LOI] Bien dich that bai!
    pause
    exit /b %errorlevel%
)
echo Bien dich thanh cong! Dang chay Exercise09Demo...
echo.
java -Dfile.encoding=UTF-8 exercise09.Exercise09Demo
echo.
pause
