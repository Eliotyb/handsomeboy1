package com.community.groupon.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AiChatService {

    @Value("${ai.dashscope.api-key:}")
    private String apiKey;

    @Value("${ai.model:qwen-plus}")
    private String model;

    @Value("${ai.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String baseUrl;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public String chat(String userMessage, List<ChatMessage> history) throws IOException {
        if (apiKey == null || apiKey.isEmpty()) {
            return "错误: 未配置 AI API Key，请设置环境变量 ALAPI_KEY";
        }

        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage("system",
                "你是「社团团购助手」，一个专业的校园社团团购平台AI助手。" +
                "你的职责包括：" +
                "1. 回答关于社团管理、活动组织、团购运营的问题；" +
                "2. 帮助用户理解和使用本平台的各项功能（商品管理、秒杀活动、订单处理等）；" +
                "3. 提供校园活动的创意建议和方案策划；" +
                "4. 回答技术问题（Java/Spring Boot/Vue/MySQL等）。" +
                "请用友好、专业、简洁的中文回答，必要时使用emoji增加亲和力。"
        ));

        if (history != null) {
            messages.addAll(history);
        }

        messages.add(new ChatMessage("user", userMessage));

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        RequestBody body = RequestBody.create(
                requestBody.toJSONString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject result = JSON.parseObject(responseBody);

            if (result.containsKey("choices")) {
                JSONArray choices = result.getJSONArray("choices");
                if (!choices.isEmpty()) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject msg = choice.getJSONObject("message");
                    return msg.getString("content");
                }
            }

            if (result.containsKey("error")) {
                JSONObject error = result.getJSONObject("error");
                return "AI服务错误: " + error.getString("message");
            }

            return "AI返回异常: " + responseBody.substring(0, Math.min(200, responseBody.length()));
        }
    }

    public static class ChatMessage {
        public String role;
        public String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
