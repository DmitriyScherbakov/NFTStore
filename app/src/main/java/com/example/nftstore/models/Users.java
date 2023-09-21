package com.example.nftstore.models;

public class Users {

    String nickname, email, avatar_url, balance;

    public Users(String nickname, String email, String avatar_url, String balance) {
        this.nickname = nickname;
        this.email = email;
        this.avatar_url = avatar_url;
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
}
