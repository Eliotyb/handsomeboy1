package com.community.groupon.controller;

import com.community.groupon.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AiChatController {

    @Value("${ai.dashscope.api-key:}")
    private String apiKey;

    @Value("${ai.model:qwen-plus}")
    private String model;

    @PostMapping("/chat")
    public Result<String> chat(@RequestBody Map<String, Object> body) {
        try {
            String message = (String) body.getOrDefault("message", "");
            if (message.trim().isEmpty()) {
                return Result.error("消息内容不能为空");
            }

            if (apiKey == null || apiKey.isEmpty()) {
                return Result.error("AI服务未配置API Key，请设置环境变量 ALAPI_KEY");
            }

            String reply = callQwenApi(message);
            return Result.success(reply);
        } catch (Exception e) {
            return Result.error("AI请求异常: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public Result<Map<String, Object>> status() {
        Map<String, Object> info = new HashMap<>();
        info.put("model", "qwen-plus");
        info.put("provider", "阿里云 DashScope");
        info.put("status", apiKey != null && !apiKey.isEmpty() ? "online" : "offline(未配置ALAPI_KEY)");
        info.put("configured", apiKey != null && !apiKey.isEmpty());
        return Result.success(info);
    }

    private String callQwenApi(String userMessage) throws IOException {
        StringBuilder result = new StringBuilder();
        java.net.URL url = new java.net.URL("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions");
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> sysMsg = new HashMap<>();
        sysMsg.put("role", "system");
        sysMsg.put("content", "你是「社团团购助手」，一个专业的校园社团团购平台AI助手。请用友好、专业、简洁的中文回答。");
        messages.add(sysMsg);
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String jsonBody = mapper.writeValueAsString(body);

        try (java.io.OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        if (code == 200) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
            Map<String, Object> resp = mapper.readValue(result.toString(), Map.class);
            List<?> choices = (List<?>) resp.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                Map<?, ?> msg = (Map<?, ?>) choice.get("message");
                return (String) msg.get("content");
            }
            return "AI返回: " + result.substring(0, Math.min(200, result.length()));
        } else {
            return "AI服务错误(HTTP " + code + ")";
        }
    }
}
