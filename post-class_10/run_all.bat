@echo off
chcp 65001 > nul
echo ===================================================
echo [POST_CLASS_10] CHAY THU NGHIEM CAC EXERCISE 8.1 - 8.4
echo ===================================================

if not exist bin\designbook\solutions8\Exercise01.class (
    echo Bin chua duoc bien dich, dang tien hanh bien dich...
    call compile.bat
)

echo.
echo ---------------------------------------------------
echo [1] CHAY EXERCISE 8.1 (Push Strategy)
echo ---------------------------------------------------
java -cp bin designbook.solutions8.Exercise01

echo.
echo ---------------------------------------------------
echo [2] CHAY EXERCISE 8.2 (Pull Strategy + ISP DeckView)
echo ---------------------------------------------------
java -cp bin designbook.solutions8.Exercise02

echo.
echo ---------------------------------------------------
echo [3] CHAY EXERCISE 8.3 (Superclass: java.util.Observable)
echo ---------------------------------------------------
java -cp bin designbook.solutions8.Exercise03

echo.
echo ---------------------------------------------------
echo [4] CHAY EXERCISE 8.4 (Subclass: ObservableDeck extends Deck)
echo ---------------------------------------------------
java -cp bin designbook.solutions8.Exercise04

echo.
echo ===================================================
echo [HOAN TAT] Da thuc thi thanh cong tat ca cac bai tap!
echo ===================================================
pause

