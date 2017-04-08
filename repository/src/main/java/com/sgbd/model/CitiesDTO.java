package com.sgbd.model;

import java.util.List;

/**
 * Created by mihae on 4/8/2017.
 */
public class CitiesDTO {

    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private String[][] data;

    public CitiesDTO(int draw, int recordsTotal, int recordsFiltered, String[][] data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public CitiesDTO(){}

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
}
