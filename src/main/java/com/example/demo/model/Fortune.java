package com.example.demo.model;

public class Fortune {
    private int id;
    private String fortuneLevel;
    private String poem;

    public Fortune() {}

    public Fortune(int id, String fortuneLevel, String poem) {
        this.id = id;
        this.fortuneLevel = fortuneLevel;
        this.poem = poem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFortuneLevel() { return fortuneLevel; }
    public void setFortuneLevel(String fortuneLevel) { this.fortuneLevel = fortuneLevel; }

    public String getPoem() { return poem; }
    public void setPoem(String poem) { this.poem = poem; }
}