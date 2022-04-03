package com.example.studiograficzne;

public class UserGameInfo {
    private String UID;
    private String nick;
    private double level;
    private double money;
    private double experience;
    private double resources;

    //pusty konstruktor
    public UserGameInfo() {
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    //Gettery i settery
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getResources() {
        return resources;
    }

    public void setResources(double resources) {
        this.resources = resources;
    }
}

