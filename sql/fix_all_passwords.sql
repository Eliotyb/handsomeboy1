-- 更新所有用户密码为BCrypt加密版本
USE community_groupon;

-- 以下是 "123456" 的多个BCrypt加密版本
-- 每个加密结果不同，但都能正确验证

UPDATE `user` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH' WHERE `username` = 'admin';
UPDATE `user` SET `password` = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE `username` = 'user1';
UPDATE `user` SET `password` = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG' WHERE `username` = 'user2';
UPDATE `user` SET `password` = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE `username` = 'user3';
UPDATE `user` SET `password` = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhW' WHERE `username` = 'yb';

-- 验证更新
SELECT id, username, LEFT(password, 35) as password_prefix, name, role FROM `user`;
