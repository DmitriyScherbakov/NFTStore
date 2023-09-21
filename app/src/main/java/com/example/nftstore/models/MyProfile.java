package com.example.nftstore.models;

public class MyProfile {
    private String nickname, email, password1, password2, avatar_url, balance;

    public MyProfile(String nickname, String email, String password1, String password2, String avatar_url, String balance) {
        this.nickname = nickname;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.avatar_url = avatar_url;
        this.balance = balance;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
