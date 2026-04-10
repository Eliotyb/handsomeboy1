@echo off
cd /d %~dp0backend

REM Set JAVA_HOME
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_144

REM Build classpath with Spring Boot starters
set CP=target/classes
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot\2.7.15\spring-boot-2.7.15.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\2.7.15\spring-boot-autoconfigure-2.7.15.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-starter\2.7.15\spring-boot-starter-2.7.15.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-starter-web\2.7.15\spring-boot-starter-web-2.7.15.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-starter-data-redis\2.7.15\spring-boot-starter-data-redis-2.7.15.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-starter-security\2.7.15\spring-boot-starter-security-2.7.15.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\com\baidu\mybatis-plus\mybatis-plus-boot-starter\3.5.3\mybatis-plus-boot-starter-3.5.3.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar
set CP=%CP%;%USERPROFILE%\.m2\repository\io\jsonwebtoken\jjwt\0.9.1\jjwt-0.9.1.jar

REM Add all Spring framework JARs using wildcard
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\*
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\security\*
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\data\*
set CP=%CP%;%USERPROFILE%\.m2\repository\org\springframework\boot\*

REM Compile
echo Compiling...
"%JAVA_HOME%\bin\javac.exe" -cp "%CP%" -d target/classes src\main\java\com\community\groupon\*.java

if errorlevel 1 (
    echo Compilation failed but trying to continue...
) else (
    echo Compilation successful!
)

REM Run
echo Starting Spring Boot application...
"%JAVA_HOME%\bin\java.exe" -cp "%CP%" com.community.groupon.GrouponApplication
