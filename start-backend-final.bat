@echo off
SETLOCAL
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_144
SET MAVEN_HOME=%~dp0apache-maven-3.8.6
SET PATH=%MAVEN_HOME%\bin;%PATH%
cd /d %~dp0backend
call ..\apache-maven-3.8.6\bin\mvn.cmd spring-boot:run
