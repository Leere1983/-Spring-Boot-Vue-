package com.spboot.app.pojo;

/**
 *  修改密码提交的数据
 */
public class PasswordPojo {

    // 提交的原密码
    private String oldPassword;
    // 提交的新密码
    private String newPassword;
    // 提交的确认密码
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
