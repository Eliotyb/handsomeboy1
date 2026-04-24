package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.service.OrderService;
import com.community.groupon.service.ProductService;
import com.community.groupon.service.SeckillActivityService;
import com.community.groupon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private OrderService orderService;

    @Value("${ai.dashscope.api-key:}")
    private String apiKey;

    @Value("${ai.model:qwen-plus}")
    private String aiModel;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long userCount = userService.count();
        long productCount = productService.count();
        long seckillActivityCount = seckillActivityService.count();
        long orderCount = orderService.count();
        Double totalAmount = orderService.getTotalAmount();

        stats.put("userCount", userCount);
        stats.put("productCount", productCount);
        stats.put("seckillActivityCount", seckillActivityCount);
        stats.put("orderCount", orderCount);
        stats.put("totalAmount", totalAmount != null ? String.format("%.0f", totalAmount) : "0");

        return Result.success(stats);
    }

    @GetMapping("/ai-status")
    public Result<Map<String, Object>> getAiStatus() {
        Map<String, Object> info = new HashMap<>();
        info.put("model", "qwen-plus");
        info.put("provider", "阿里云 DashScope");
        info.put("status", apiKey != null && !apiKey.isEmpty() ? "online" : "offline");
        return Result.success(info);
    }

    @PostMapping("/ai-chat")
    public Result<String> aiChat(@RequestBody Map<String, Object> body) {
        try {
            String message = (String) body.getOrDefault("message", "");
            if (message.trim().isEmpty()) {
                return Result.error("消息内容不能为空");
            }
            if (apiKey == null || apiKey.isEmpty()) {
                return Result.error("AI服务未配置API Key");
            }
            String reply = callQwenApi(message);
            return Result.success(reply);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("AI异常: " + e.getMessage());
        }
    }

    private String callQwenApi(String userMessage) throws Exception {
        URL url = new URL("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> sysMsg = new HashMap<>();
        sysMsg.put("role", "system");
        sysMsg.put("content", "你是「社团团购助手」，专业的校园社团团购平台AI助手。用友好、专业、简洁的中文回答。");
        messages.add(sysMsg);
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiModel);
        requestBody.put("messages", messages);

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String jsonBody = mapper.writeValueAsString(requestBody);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes("utf-8"));
        }

        int code = conn.getResponseCode();
        StringBuilder result = new StringBuilder();
        if (code == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
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
            return "AI: " + result.substring(0, Math.min(200, result.length()));
        } else {
            return "AI错误(HTTP " + code + ")";
        }
    }
}
