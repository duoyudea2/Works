package com.example.demo.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String placeOfBirth;
    private String birthDatetime;
    private String bazi;

    public User() {}

    public User(String username, String password, String placeOfBirth, String birthDatetime, String bazi) {
        this.username = username;
        this.password = password;
        this.placeOfBirth = placeOfBirth;
        this.birthDatetime = birthDatetime;
        this.bazi = bazi;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public String getBirthDatetime() { return birthDatetime; }
    public void setBirthDatetime(String birthDatetime) { this.birthDatetime = birthDatetime; }

    public String getBazi() { return bazi; }
    public void setBazi(String bazi) { this.bazi = bazi; }
}