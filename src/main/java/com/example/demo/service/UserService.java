package com.example.demo.service;

import java.util.concurrent.ConcurrentHashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import com.example.demo.util.DeepSeekClient;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DeepSeekClient deepSeekClient;

    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    public void register(String username, String password,
            String placeOfBirth, String birthDatetime) throws Exception {
        Object lock = locks.computeIfAbsent(username, k -> new Object());
        synchronized (lock) {
            try {
                if (userDao.findByUsername(username) != null) {
                    throw new Exception("用户名已存在");
                }

                String birthInfo = "出生地：" + placeOfBirth + "，出生时间：" + birthDatetime;
                String bazi = deepSeekClient.toBaZi(birthInfo);

                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

                User user = new User(username, hashed, placeOfBirth, birthDatetime, bazi);
                userDao.register(user);
            } finally {
                locks.remove(username);
            }
        }
    }

    public User login(String username, String password) throws Exception {
        User user = userDao.findByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("用户名或密码错误");
        }
        return user;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
