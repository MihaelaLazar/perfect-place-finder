package com.sgbd.dto;

import com.sgbd.model.Attachement;

import java.io.Serializable;
import java.util.Set;

public class EstateDTO implements Serializable {

    private String realEstateType;
    private Long surface;
    private Long roomsNumber;
    private Long rentPrice;
    private Long buyPrice;
    private Long constructionYear;
    private String description;
    private Double addressLat;
    private Double addressLng;
    private String division;
    private String city;
    private Long utilities;
    private String contactNumber;
    private Set<Attachement> attachements;
    private Long levelOfComfort;
    private Long bathrooms;
    private Long carDisposal;
    private Long floor;
    private Set<String> announcementImagesArray ;
    private Set<String> announcementImagesIconsURIArray;


    public EstateDTO() {
    }

    public Set<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(Set<Attachement> attachements) {
        this.attachements = attachements;
    }

    public String getRealEstateType() {
        return realEstateType;
    }

    public void setRealEstateType(String realEstateType) {
        this.realEstateType = realEstateType;
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

    public Long getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Long constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAddressLat() {
        return addressLat;
    }

    public void setAddressLat(Double addressLat) {
        this.addressLat = addressLat;
    }

    public Double getAddressLng() {
        return addressLng;
    }

    public void setAddressLng(Double addressLng) {
        this.addressLng = addressLng;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getUtilities() {
        return utilities;
    }

    public void setUtilities(Long utilities) {
        this.utilities = utilities;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public Set<String> getAnnouncementImagesIconsURIArray() {
        return announcementImagesIconsURIArray;
    }

    public void setAnnouncementImagesIconsURIArray(Set<String> announcementImagesIconsURIArray) {
        this.announcementImagesIconsURIArray = announcementImagesIconsURIArray;
    }
}
