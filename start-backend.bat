@echo off
cd /d %~dp0\backend

REM Check if Maven is installed
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven found, running project...
    mvn spring-boot:run
) else (
    echo Maven not found, trying to run with Java directly...
    
    REM Try to find and run with all dependencies
    echo Building classpath from Maven repository...
    set CLASSPATH=target/classes
    
    REM Add all JARs from Maven repository
    for /r "C:\Users\杨彬\.m2\repository" %%i in (*.jar) do set CLASSPATH=!CLASSPATH!;%%i
    
    echo Starting Spring Boot application...
    java com.community.groupon.GrouponApplication
)
