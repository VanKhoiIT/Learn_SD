@echo off
chcp 65001 > nul
cd /d "%~dp0"
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "bin;lib/*" lab09.orderprocessing.TestRunner
pause

