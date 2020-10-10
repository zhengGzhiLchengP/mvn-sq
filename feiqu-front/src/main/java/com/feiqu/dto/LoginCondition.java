package com.feiqu.dto;

public class LoginCondition {
    private String username;
    private String password;
    private String verifyCode;
    private String remember;
    private String rsUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

    public String getRsUrl() {
        return rsUrl;
    }

    public void setRsUrl(String rsUrl) {
        this.rsUrl = rsUrl;
    }
}
