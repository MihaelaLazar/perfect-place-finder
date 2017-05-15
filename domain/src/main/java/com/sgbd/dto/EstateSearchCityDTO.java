package com.sgbd.dto;

import com.sgbd.model.Estate;


public class EstateSearchCityDTO {

    private Estate estate;
    private Long isFavorite;

    public EstateSearchCityDTO() {
    }

    public EstateSearchCityDTO(Estate estate, Long isFavorite) {
        this.estate = estate;
        this.isFavorite = isFavorite;
    }

    public Estate getEstate() {
        return estate;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public Long getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Long isFavorite) {
        this.isFavorite = isFavorite;
    }
}
