package com.sgbd.dto;

public class MessageDTO {

    private String dateToMove;
    private String email;
    private String phone;
    private String name;
    private Long estateId;

    public MessageDTO() {
    }

    public MessageDTO(String dateToMove, String email, String phone, String name, Long estateId) {
        this.dateToMove = dateToMove;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.estateId = estateId;
    }

    public String getDateToMove() {
        return dateToMove;
    }

    public void setDateToMove(String dateToMove) {
        this.dateToMove = dateToMove;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEstateId() {
        return estateId;
    }

    public void setEstateId(Long estateId) {
        this.estateId = estateId;
    }

}
