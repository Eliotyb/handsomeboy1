@echo off
echo ========================================
echo 数据库密码更新脚本
echo ========================================
echo.
echo 请选择操作:
echo 1. 更新现有数据库密码为BCrypt加密
echo 2. 重新导入包含加密密码的完整数据
echo 3. 退出
echo.
set /p choice=请输入选项 (1/2/3):

if "%choice%"=="1" goto update_passwords
if "%choice%"=="2" goto import_all
if "%choice%"=="3" goto end

echo 无效选项
pause
goto end

:update_passwords
echo.
echo 正在更新用户密码...
echo 请确保您已连接到 MySQL 数据库
echo.
echo 执行的 SQL 语句:
echo USE community_groupon;
echo UPDATE `user` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH' WHERE `username` = 'admin';
echo UPDATE `user` SET `password` = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE `username` = 'user1';
echo UPDATE `user` SET `password` = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG' WHERE `username` = 'user2';
echo UPDATE `user` SET `password` = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE `username` = 'user3';
echo.
echo 请手动在 MySQL 客户端中执行上述 SQL 语句
echo.
pause
goto end

:import_all
echo.
echo 正在准备重新导入数据...
echo.
echo 文件位置: sql\sample_data_encrypted.sql
echo.
echo 请手动在 MySQL 客户端中执行以下命令:
echo 1. DROP DATABASE IF EXISTS community_groupon;
echo 2. CREATE DATABASE community_groupon;
echo 3. USE community_groupon;
echo 4. 执行 create_tables.sql
echo 5. 执行 sample_data_encrypted.sql
echo.
pause

:end
echo.
echo 完成！
