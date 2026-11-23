[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "[POST_CLASS_10] CHẠY THỬ NGHIỆM CÁC EXERCISE 8.1 - 8.4" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin\designbook\solutions8\Exercise01.class")) {
    Write-Host "Thư mục bin chưa có class, đang biên dịch..." -ForegroundColor Yellow
    javac -encoding UTF-8 -d bin src\designbook\chapter8\*.java src\designbook\solutions8\*.java
}

Write-Host "`n---------------------------------------------------" -ForegroundColor Green
Write-Host "[1] CHẠY EXERCISE 8.1 (Push Strategy)" -ForegroundColor Green
Write-Host "---------------------------------------------------" -ForegroundColor Green
java -cp bin designbook.solutions8.Exercise01

Write-Host "`n---------------------------------------------------" -ForegroundColor Green
Write-Host "[2] CHẠY EXERCISE 8.2 (Pull Strategy + ISP DeckView)" -ForegroundColor Green
Write-Host "---------------------------------------------------" -ForegroundColor Green
java -cp bin designbook.solutions8.Exercise02

Write-Host "`n---------------------------------------------------" -ForegroundColor Green
Write-Host "[3] CHẠY EXERCISE 8.3 (Superclass: java.util.Observable)" -ForegroundColor Green
Write-Host "---------------------------------------------------" -ForegroundColor Green
java -cp bin designbook.solutions8.Exercise03

Write-Host "`n---------------------------------------------------" -ForegroundColor Green
Write-Host "[4] CHẠY EXERCISE 8.4 (Subclass: ObservableDeck extends Deck)" -ForegroundColor Green
Write-Host "---------------------------------------------------" -ForegroundColor Green
java -cp bin designbook.solutions8.Exercise04

Write-Host "`n===================================================" -ForegroundColor Cyan
Write-Host "[HOÀN TẤT] Toàn bộ 4 bài tập đã chạy thành công!" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

