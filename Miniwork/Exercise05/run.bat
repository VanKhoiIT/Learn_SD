@echo off
chcp 65001 > nul
title Exercise 05 - Metaprogramming & Reflection (Miniwork)

cd /d "%~dp0"

:: 1. Kiểm tra môi trường JDK
where javac >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [LỖI] Không tìm thấy 'javac' trong biến môi trường PATH!
    echo Vui lòng cài đặt JDK 17, 21 hoặc 25 và thiết lập PATH.
    pause
    exit /b 1
)

:: 2. Biên dịch mã nguồn Java
echo ================================================================
echo       ĐANG BIÊN DỊCH MÃ NGUỒN JAVA (EXERCISE 05)...
echo ================================================================
javac -encoding UTF-8 -d . *.java
if %ERRORLEVEL% neq 0 (
    echo [LỖI] Biên dịch thất bại!
    pause
    exit /b %ERRORLEVEL%
)
echo [OK] Biên dịch thành công!
echo.

:: 3. Nếu truyền tham số trực tiếp qua dòng lệnh (ví dụ: run.bat Integer 5)
if not "%~1"=="" (
    if /i "%~1"=="--test" (
        java -Dfile.encoding=UTF-8 designbook.solutions5.TestRunner
        exit /b %ERRORLEVEL%
    )
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 %*
    exit /b %ERRORLEVEL%
)

:MENU
echo ================================================================
echo    MINIWORK - EXERCISE 05: METAPROGRAMMING & JAVA REFLECTION
echo ================================================================
echo 1. Chạy ca kiểm thử mẫu: Integer 5
echo 2. Chạy ca kiểm thử mẫu: Double 5.1
echo 3. Chạy ca kiểm thử mẫu: Float 3.14
echo 4. Chạy ca kiểm thử mẫu: Long 1000
echo 5. Chạy ca kiểm thử mẫu: Byte 127
echo 6. Chạy ca kiểm thử mẫu: Short 32000
echo 7. Chạy ca kiểm thử lỗi: Integer abc (Sai định dạng)
echo 8. Chạy ca kiểm thử lỗi: String 123 (Không thuộc Number)
echo 9. Chạy bộ 12 ca kiểm thử tự động (TestRunner)
echo 10. Nhập tham số tùy chọn (Kiểu và Giá trị)
echo 0. Thoát
echo ================================================================
set /p choice="Nhập lựa chọn của bạn (0-10): "

if "%choice%"=="1" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Integer 5
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Integer 5
    echo.
    pause
    goto MENU
)

if "%choice%"=="2" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Double 5.1
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Double 5.1
    echo.
    pause
    goto MENU
)

if "%choice%"=="3" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Float 3.14
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Float 3.14
    echo.
    pause
    goto MENU
)

if "%choice%"=="4" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Long 1000
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Long 1000
    echo.
    pause
    goto MENU
)

if "%choice%"=="5" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Byte 127
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Byte 127
    echo.
    pause
    goto MENU
)

if "%choice%"=="6" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Short 32000
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Short 32000
    echo.
    pause
    goto MENU
)

if "%choice%"=="7" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Integer abc
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Integer abc
    echo.
    pause
    goto MENU
)

if "%choice%"=="8" (
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 String 123
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 String 123
    echo.
    pause
    goto MENU
)

if "%choice%"=="9" (
    echo.
    java -Dfile.encoding=UTF-8 designbook.solutions5.TestRunner
    echo.
    pause
    goto MENU
)

if "%choice%"=="10" (
    echo.
    set /p mytype="Nhập tên kiểu con của Number (ví dụ: Integer, Double, Long): "
    set /p myval="Nhập chuỗi giá trị số (ví dụ: 100, 3.14159, -50): "
    echo.
    echo Running: java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 !mytype! !myval!
    java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 !mytype! !myval!
    echo.
    pause
    goto MENU
)

if "%choice%"=="0" (
    echo Tạm biệt!
    exit /b 0
)

echo Lựa chọn không hợp lệ, vui lòng thử lại!
echo.
goto MENU
