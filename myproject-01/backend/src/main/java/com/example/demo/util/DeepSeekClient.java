package com.example.demo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class DeepSeekClient {

    @Value("${deepseek.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.deepseek.com/chat/completions";

    public String ask(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);

        JsonObject root = new JsonObject();
        root.addProperty("model", "deepseek-v4-pro");

        JsonArray messages = new JsonArray();
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", prompt);
        messages.add(userMsg);

        root.add("messages", messages);
        root.addProperty("temperature", 0.8);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(root.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        BufferedReader br;
        if (code == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        Gson gson = new Gson();
        JsonObject resp = gson.fromJson(sb.toString(), JsonObject.class);

        if (resp.has("choices")) {
            return resp.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        } else {
            return "AI 调用失败：" + sb;
        }
    }

    public String toBaZi(String birthInfo) throws Exception {
        String prompt = "请根据以下出生信息，先转换成中国北京时间，再计算对应的八字（四柱）："
                + birthInfo
                + "。只返回八字结果，格式如：戊戌年、乙未月、乙卯日、甲午时。不要多余解释。";
        return ask(prompt);
    }

    public String interpretPoem(String poem, String interests, String bazi) throws Exception {
        String prompt = """
        你是一位精通八字命理的分析师，擅长将传统命理与现代生活结合。

        ## 用户信息
        - 八字：%s
        - 关注领域：%s

        ## 签诗原文
        %s

        ## 解读要求
        1. 先解释签诗的字面含义
        2. 结合用户八字，分析签诗对用户的启示
        3. 针对用户关注的领域，给出 2-3 条具体建议
        4. 语气温暖、积极，避免宿命论
        5. 控制在 300 字以内

        ## 输出格式
        直接输出解读内容，不要加标题。
        """.formatted(bazi, interests, poem);
        return ask(prompt);
    }

    public String translate(String text, String language) throws Exception {
        String prompt = "请把下面这段文字翻译成" + language + "，只返回翻译结果，不要解释：\n" + text;
        return ask(prompt);
    }
}