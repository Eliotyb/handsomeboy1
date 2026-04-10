package com.community.groupon.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String plainPassword = "123456";
        
        System.out.println("=== 密码生成器 ===");
        System.out.println("原始密码: " + plainPassword);
        System.out.println();
        
        for (int i = 0; i < 5; i++) {
            String encoded = encoder.encode(plainPassword);
            System.out.println("加密密码 " + (i + 1) + ": " + encoded);
            
            boolean matches = encoder.matches(plainPassword, encoded);
            System.out.println("匹配验证: " + (matches ? "✅ 通过" : "❌ 失败"));
            System.out.println();
        }
        
        System.out.println("=== SQL 更新语句 ===");
        System.out.println("USE community_groupon;");
        System.out.println();
        System.out.println("UPDATE `user` SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH' WHERE `username` = 'admin';");
        System.out.println("UPDATE `user` SET `password` = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi' WHERE `username` = 'user1';");
        System.out.println("UPDATE `user` SET `password` = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG' WHERE `username` = 'user2';");
        System.out.println("UPDATE `user` SET `password` = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE `username` = 'user3';");
        System.out.println();
        System.out.println("SELECT id, username, LEFT(password, 20) as password_prefix, name, role FROM `user`;");
    }
}
