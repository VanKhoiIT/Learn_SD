@echo off
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8
javac -encoding UTF-8 -d bin src/strategy/*.java src/strategy/impl/*.java src/service/*.java src/test/*.java src/Main.java
java -cp bin Main
