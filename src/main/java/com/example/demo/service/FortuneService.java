package com.example.demo.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.FortuneDao;
import com.example.demo.model.Fortune;
import com.example.demo.model.User;
import com.example.demo.util.DeepSeekClient;

@Service
public class FortuneService {                                                                                                                                                                           

    @Autowired
    private FortuneDao fortuneDao;

    @Autowired
    private DeepSeekClient deepSeekClient;

    // 线程池：核心 4 线程，最大 8 线程
    private final ExecutorService executor = new ThreadPoolExecutor(
            4, 8, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public Map<String, Object> getFortune(User user, String interests) throws Exception {
        int poemId = calculatePoemId(user.getBazi(), LocalDate.now().toString());
        Fortune fortune = fortuneDao.findById(poemId);

        String interpretation = deepSeekClient.interpretPoem(
                fortune.getPoem(), interests, user.getBazi());

        //并行翻译英文和日文
        CompletableFuture<String> enFuture = CompletableFuture.supplyAsync(() -> {
            try { return deepSeekClient.translate(interpretation, "English"); }
            catch (Exception e) { return "Translation failed."; }
        }, executor);

        CompletableFuture<String> jpFuture = CompletableFuture.supplyAsync(() -> {
            try { return deepSeekClient.translate(interpretation, "Japanese"); }
            catch (Exception e) { return "翻訳に失敗しました。"; }
        }, executor);

        // 等两个都完成
        String english = enFuture.get(30, TimeUnit.SECONDS);
        String japanese = jpFuture.get(30, TimeUnit.SECONDS);

        Map<String, Object> result = new HashMap<>();
        result.put("fortune", fortune);
        result.put("interests", interests);
        result.put("interpretation", interpretation);
        result.put("english", english);
        result.put("japanese", japanese);
        return result;
    }
    private int calculatePoemId(String bazi, String date) {
        String combined = (bazi == null ? "" : bazi) + date;
        int hash = 0;
        for (char c : combined.toCharArray()) {
            hash = (hash * 31 + c) % 96;
        }
        return Math.abs(hash) + 1;
    }
}