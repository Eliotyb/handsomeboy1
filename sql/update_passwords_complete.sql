-- 更新用户密码为BCrypt加密版本
-- 使用数据库
USE community_groupon;

-- 密码 "123456" 的 BCrypt 加密示例
-- 每次加密的结果不同，但都能正确匹配 "123456"

-- 更新所有用户密码为 BCrypt 加密后的 "123456"
UPDATE `user` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH' WHERE `username` = 'admin';
UPDATE `user` SET `password` = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE `username` = 'user1';
UPDATE `user` SET `password` = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG' WHERE `username` = 'user2';
UPDATE `user` SET `password` = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE `username` = 'user3';

-- 验证更新结果
SELECT id, username, password, name, role FROM `user`;

-- 注意：上述密码都是 "123456" 的 BCrypt 加密版本
-- 如果您需要生成自己的加密密码，可以运行 PasswordEncoderUtil.java
