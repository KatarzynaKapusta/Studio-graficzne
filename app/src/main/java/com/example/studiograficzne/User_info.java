package com.example.studiograficzne;

/**
 * user class to keep track of registered user
 */

public class User_info {
    private String email;
    private String nickname;
    private String password;

    public User_info() {}

    public User_info(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getpassword() {
        return password;
    }
}