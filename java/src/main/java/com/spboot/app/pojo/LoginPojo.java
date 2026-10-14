package com.spboot.app.pojo;

/**
 *  登录提交的数据实体类结构数据
 *
 */
public class LoginPojo {

    // 提交的账号
    private String username;
    // 密码
    private String pwd;
    // 登录类型
    private String cx;
    // 验证码
    private String a;
    // 提交的验证码值
    private String pagerandom;
    // 后端给到的验证码加密数据
    private String captchToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getPagerandom() {
        return pagerandom;
    }

    public void setPagerandom(String pagerandom) {
        this.pagerandom = pagerandom;
    }

    public String getCaptchToken() {
        return captchToken;
    }

    public void setCaptchToken(String captchToken) {
        this.captchToken = captchToken;
    }
}
