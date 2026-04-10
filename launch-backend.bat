@echo off
SETLOCAL ENABLEDELAYEDEXPANSION
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_144
cd /d %~dp0backend

REM Build classpath with all JARs from Maven repository
set CP=target/classes
for /r "C:\Users\杨彬\.m2\repository" %%f in (*.jar) do (
    set CP=!CP!;%%f
)

echo Starting backend...
"%JAVA_HOME%\bin\java.exe" -cp "!CP!" com.community.groupon.GrouponApplication
