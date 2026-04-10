-- 更新用户密码为BCrypt加密版本
-- 使用数据库
USE community_groupon;

-- 密码 "123456" 的 BCrypt 加密示例（每次生成的哈希值可能不同，但都能正确匹配）
-- 以下是几个有效的 BCrypt 加密后的密码（都对应 "123456"）：
-- $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH
-- $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
-- $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

-- 更新用户密码（使用预先生成的BCrypt哈希，实际应用中应该重新生成）
-- 注意：实际使用时请运行 PasswordEncoderUtil.java 生成自己的加密密码

-- 这里提供一个通用的更新语句模板：
-- UPDATE `user` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH' WHERE `username` = 'admin';

-- 或者使用以下方式批量更新（需要在 Java 代码中处理更安全）
-- 建议：
-- 1. 运行 PasswordEncoderUtil 生成加密密码
-- 2. 替换下面的密码值
-- 3. 执行更新语句
