package com.sgbd.model;

/**
 * Created by mihae on 4/8/2017.
 */
public class Estate {

    private int ID;
    private String type;
    private String address;
    private int surface;
    private int rooms;
    private int rentPrice;
    private int buyPrice;
    private String division;
    private int constructionYear;
    private String description;
    private String creationDate;
    private String lastUpdate;
    private String city;

    public Estate(int ID, String type, String address, int surface, int rooms, int rentPrice, int buyPrice, String division, int constructionYear, String description, String creationDate, String lastUpdate, String city) {
        this.ID = ID;
        this.type = type;
        this.address = address;
        this.surface = surface;
        this.rooms = rooms;
        this.rentPrice = rentPrice;
        this.buyPrice = buyPrice;
        this.division = division;
        this.constructionYear = constructionYear;
        this.description = description;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
        this.city = city;
    }

    public Estate() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
