package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.FortuneService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/fortune")
public class FortuneController {

    @Autowired
    private FortuneService fortuneService;

    @Autowired
    private UserService userService;

    @PostMapping("/get")
    public Map<String, Object> getFortune(@RequestBody Map<String, String> body,
            HttpServletRequest request) {
        Map<String, Object> resp = new HashMap<>();
        String username = (String) request.getAttribute("username");
        try {
            User user = userService.findByUsername(username);
            String interests = body.getOrDefault("interests", "综合运势");
            Map<String, Object> result = fortuneService.getFortune(user, interests);
            resp.put("code", 200);
            resp.putAll(result);
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("code", 500);
            resp.put("msg", e.getMessage());
        }
        return resp;
    }
}
