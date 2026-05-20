@echo off
echo === Compiling Chess Championship UI ===
cd /d "%~dp0"

:: Create output directory
if not exist out mkdir out

:: Compile all Java files
javac -d out -sourcepath . model\*.java dao\*.java view\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo.
echo === Running LoginFrm ===
java -cp out view.LoginFrm

pause
