# Thiết lập bảng mã UTF-8 cho PowerShell console
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "             DEMO MẪU THIẾT KẾ TEMPLATE METHOD: LAB_09 (UTF-8)                  " -ForegroundColor Yellow
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host ""

java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" "-Dstderr.encoding=UTF-8" -cp "bin;lib/*" lab09.orderprocessing.MainDemo

