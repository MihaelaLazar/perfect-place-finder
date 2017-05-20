package com.sgbd.dto;

/**
 * Created by Rares on 5/14/2017.
 */
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long idUser;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String firstName, String lastName, String email, Long idUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
