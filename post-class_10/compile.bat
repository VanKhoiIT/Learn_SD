@echo off
chcp 65001 > nul
echo ===================================================
echo [POST_CLASS_10] BIEN DICH CAC BAI TAP CHUONG 8
echo ===================================================

if not exist bin mkdir bin

javac -encoding UTF-8 -d bin src\designbook\chapter8\*.java src\designbook\solutions8\*.java

if %ERRORLEVEL% equ 0 (
    echo [THANH CONG] Bien dich thanh cong toan bo source code vao thu muc bin/
) else (
    echo [THAT BAI] Co loi trong qua trinh bien dich!
)

