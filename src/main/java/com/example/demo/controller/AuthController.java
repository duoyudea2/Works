package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        try {
            userService.register(
                    body.get("username"),
                    body.get("password"),
                    body.get("placeOfBirth"),
                    body.get("birthDatetime")
            );
            resp.put("code", 200);
            resp.put("msg", "注册成功");
        } catch (Exception e) {
            resp.put("code", 500);
            resp.put("msg", e.getMessage());
        }
        return resp;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        try {
            User user = userService.login(body.get("username"), body.get("password"));
            String token = tokenService.generateToken(user.getId(), user.getUsername());
            resp.put("code", 200);
            resp.put("msg", "登录成功");
            resp.put("token", token);
            resp.put("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("code", 500);
            resp.put("msg", e.getMessage());
        }
        return resp;
    }

    @GetMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            tokenService.deleteToken(token.substring(7));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("msg", "已退出");
        return resp;
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.findByUsername(username);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("username", user.getUsername());
        resp.put("placeOfBirth", user.getPlaceOfBirth());
        resp.put("birthDatetime", user.getBirthDatetime());
        resp.put("bazi", user.getBazi());
        return resp;
    }
}
