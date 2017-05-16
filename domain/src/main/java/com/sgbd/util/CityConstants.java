package com.sgbd.util;

public enum CityConstants {

    IASI("Iasi"),
    BUCURESTI("Bucuresti"),
    LONDON("London"),
    NEWYORK("New York");


    private String city;

    CityConstants(String city) {
        this.city = city;
    }

    public String getAttachType() {
        return city;
    }
}
