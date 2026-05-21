@echo off
echo === Compiling Chess Championship - Login Demo ===
cd /d "%~dp0"

:: Create output directory
if not exist out mkdir out

:: Đường dẫn tới MySQL JDBC Driver JAR
set JDBC_JAR=lib\mysql-connector-j-8.4.0.jar

:: Compile chỉ các file cần thiết cho chức năng Login
javac -cp "%JDBC_JAR%" -d out model\User.java dao\DBConnection.java dao\UserDAO.java view\LoginFrm.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo.
echo === Running Login Demo ===
java -cp "out;%JDBC_JAR%" view.LoginFrm

pause
