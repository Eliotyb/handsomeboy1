package com.community.groupon.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.Base64;

@Service
public class CaptchaService {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final int CAPTCHA_WIDTH = 120;
    private static final int CAPTCHA_HEIGHT = 40;
    private static final int CAPTCHA_CODE_COUNT = 4;
    private static final int CAPTCHA_LINE_COUNT = 5;
    private static final long CAPTCHA_EXPIRE_MINUTES = 5;

    public CaptchaResult generateCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(
            CAPTCHA_WIDTH,
            CAPTCHA_HEIGHT,
            CAPTCHA_CODE_COUNT,
            CAPTCHA_LINE_COUNT
        );

        String captchaKey = IdUtil.fastSimpleUUID();
        String captchaCode = captcha.getCode();

        saveCaptchaToRedis(captchaKey, captchaCode);

        byte[] imageBytes = captcha.getImageBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return new CaptchaResult(captchaKey, "data:image/png;base64," + base64Image);
    }

    private void saveCaptchaToRedis(String key, String code) {
        try {
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set(
                    CAPTCHA_PREFIX + key,
                    code.toLowerCase(),
                    CAPTCHA_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
                );
                System.out.println("[验证码] 已保存到Redis: " + key + " -> " + code);
            } else {
                System.out.println("[警告] Redis不可用，验证码存储在内存中（不推荐生产环境使用）");
            }
        } catch (Exception e) {
            System.out.println("[错误] 验证码保存失败: " + e.getMessage());
        }
    }

    public boolean validateCaptcha(String captchaKey, String userInput) {
        if (captchaKey == null || captchaKey.isEmpty()) {
            System.out.println("[验证码] 验证失败：captchaKey为空");
            return false;
        }

        if (userInput == null || userInput.isEmpty()) {
            System.out.println("[验证码] 验证失败：用户输入为空");
            return false;
        }

        try {
            String storedCode = null;

            if (redisTemplate != null) {
                storedCode = redisTemplate.opsForValue().get(CAPTCHA_PREFIX + captchaKey);
            }

            if (storedCode == null) {
                System.out.println("[验证码] 验证失败：验证码已过期或不存在");
                return false;
            }

            boolean isValid = storedCode.equalsIgnoreCase(userInput.trim());

            if (isValid) {
                redisTemplate.delete(CAPTCHA_PREFIX + captchaKey);
                System.out.println("[验证码] ✅ 验证成功，已删除Redis中的验证码");
            } else {
                System.out.println("[验证码] ❌ 验证失败：输入=" + userInput.trim() + "，正确答案=" + storedCode);
            }

            return isValid;
        } catch (Exception e) {
            System.out.println("[错误] 验证码校验异常: " + e.getMessage());
            return false;
        }
    }

    public static class CaptchaResult {
        private String captchaKey;
        private String captchaImage;

        public CaptchaResult(String captchaKey, String captchaImage) {
            this.captchaKey = captchaKey;
            this.captchaImage = captchaImage;
        }

        public String getCaptchaKey() {
            return captchaKey;
        }

        public void setCaptchaKey(String captchaKey) {
            this.captchaKey = captchaKey;
        }

        public String getCaptchaImage() {
            return captchaImage;
        }

        public void setCaptchaImage(String captchaImage) {
            this.captchaImage = captchaImage;
        }
    }
}
