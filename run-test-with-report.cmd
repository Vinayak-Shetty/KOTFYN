@echo off
setlocal

set "PROJECT_DIR=%~dp0"
set "TEST_NAME=%~1"
set "REPORT_ID=%TEST_NAME%"

if "%REPORT_ID%"=="" (
    set "REPORT_ID=TestNGSuite"
)

set "REPORT_ID=%REPORT_ID:#=-%"
set "REPORT_ID=%REPORT_ID::=-%"
set "REPORT_ID=%REPORT_ID:/=-%"
set "REPORT_ID=%REPORT_ID:\=-%"
set "REPORT_ID=%REPORT_ID: =_%"
set "REPORT_ID=%REPORT_ID:~0,60%"

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd-HHmmss"') do set "REPORT_TIMESTAMP=%%i"

set "REPORT_TITLE=Kotak Fyn - %REPORT_ID% - %REPORT_TIMESTAMP%"
if defined JENKINS_HOME (
    set "REPORT_DIR_REL=target\extent-report\%REPORT_ID%"
) else (
    set "REPORT_DIR_REL=target\extent-reports\%REPORT_ID%-%REPORT_TIMESTAMP%"
)
set "REPORT_DIR=%PROJECT_DIR%%REPORT_DIR_REL%"
set "REPORT_FILE=%REPORT_DIR%\index.html"

if exist "%REPORT_DIR%" (
    rmdir /s /q "%REPORT_DIR%"
)

if "%TEST_NAME%"=="" (
    call "%PROJECT_DIR%mvn.cmd" test "-Dextent.report.path=%REPORT_FILE%" "-Dextent.report.title=%REPORT_TITLE%"
) else (
    shift
    call "%PROJECT_DIR%mvn.cmd" test "-Dtest=%TEST_NAME%" "-Dextent.report.path=%REPORT_FILE%" "-Dextent.report.title=%REPORT_TITLE%"
)

set "TEST_EXIT_CODE=%ERRORLEVEL%"

if not exist "%REPORT_FILE%" (
    echo Extent report was not generated: %REPORT_FILE%
    exit /b 1
)

echo Extent report generated: %REPORT_FILE%
if /I "%OPEN_REPORT%"=="false" (
    echo OPEN_REPORT=false, skipping browser launch.
    echo Extent report generated at: %REPORT_FILE%
    exit /b %TEST_EXIT_CODE%
)

powershell -NoProfile -Command "Start-Process -FilePath '%REPORT_FILE%'"
if errorlevel 1 (
    echo Opening Extent report failed.
    exit /b 1
)

echo Extent report generated at: %REPORT_FILE%
exit /b %TEST_EXIT_CODE%
endlocal
