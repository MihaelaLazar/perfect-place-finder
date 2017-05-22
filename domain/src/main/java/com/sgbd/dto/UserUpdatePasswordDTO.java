package com.sgbd.dto;

/**
 * Created by Lazarm on 5/22/2017.
 */
public class UserUpdatePasswordDTO {

    private String newPassword;
    private String userPasswordConfirmed;

    public UserUpdatePasswordDTO() {
    }

    public UserUpdatePasswordDTO(String newPassword, String userPasswordConfirmed) {
        this.newPassword = newPassword;
        this.userPasswordConfirmed = userPasswordConfirmed;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserPasswordConfirmed() {
        return userPasswordConfirmed;
    }

    public void setUserPasswordConfirmed(String userPasswordConfirmed) {
        this.userPasswordConfirmed = userPasswordConfirmed;
    }
}
