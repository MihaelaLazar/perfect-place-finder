package com.sgbd.dto;

import com.sgbd.model.Attachement;

import java.util.Set;

/**
 * Created by Lazarm on 5/12/2017.
 */
public class EstateUpdateDTO {

    private Long surface;
    private Long roomsNumber;
    private Long rentPrice;
    private Long buyPrice;
    private String description;
    private Long utilities;
    private Long levelOfComfort;
    private Long bathrooms;
    private Long carDisposal;
    private Long floor;
    private Set<String> announcementImagesArray ;
//    private Set<String> announcementImagesIconsURIArray;
    private Long idEstate;
    private Long idUser;

    public EstateUpdateDTO() {
    }

    public EstateUpdateDTO( Long surface, Long roomsNumber, Long rentPrice,
                           Long buyPrice, String description, Long utilities, Long idUser,
                            Long levelOfComfort, Long bathrooms,
                           Long carDisposal, Long floor, Set<String> announcementImagesArray, Long idEstate) {
        this.surface = surface;
        this.roomsNumber = roomsNumber;
        this.rentPrice = rentPrice;
        this.buyPrice = buyPrice;
        this.description = description;
        this.utilities = utilities;
        this.levelOfComfort = levelOfComfort;
        this.bathrooms = bathrooms;
        this.carDisposal = carDisposal;
        this.floor = floor;
        this.announcementImagesArray = announcementImagesArray;
        this.idEstate = idEstate;
        this.idUser = idUser;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getSurface() {
        return surface;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
    }

    public Long getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(Long roomsNumber) {
        this.roomsNumber = roomsNumber;
    }

    public Long getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Long rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Long getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Long buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUtilities() {
        return utilities;
    }

    public void setUtilities(Long utilities) {
        this.utilities = utilities;
    }

    public Long getLevelOfComfort() {
        return levelOfComfort;
    }

    public void setLevelOfComfort(Long levelOfComfort) {
        this.levelOfComfort = levelOfComfort;
    }

    public Long getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Long bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Long getCarDisposal() {
        return carDisposal;
    }

    public void setCarDisposal(Long carDisposal) {
        this.carDisposal = carDisposal;
    }

    public Long getFloor() {
        return floor;
    }

    public void setFloor(Long floor) {
        this.floor = floor;
    }

    public Set<String> getAnnouncementImagesArray() {
        return announcementImagesArray;
    }

    public void setAnnouncementImagesArray(Set<String> announcementImagesArray) {
        this.announcementImagesArray = announcementImagesArray;
    }

    public Long getIdEstate() {
        return idEstate;
    }

    public void setIdEstate(Long idEstate) {
        this.idEstate = idEstate;
    }
}
