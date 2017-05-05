package com.sgbd.dto;

import com.sgbd.model.Estate;

import java.util.List;

/**
 * Created by Lazarm on 4/25/2017.
 */
public class PaginatedEstatesDetails {

    private Integer offset;
    private Integer totalCount;
    private List<Estate> estates;

    public PaginatedEstatesDetails() {
    }

    public PaginatedEstatesDetails(Integer offset, Integer totalCount, List<Estate> estates) {
        this.offset = offset;
        this.totalCount = totalCount;
        this.estates = estates;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Estate> getEstates() {
        return estates;
    }

    public void setEstates(List<Estate> estates) {
        this.estates = estates;
    }
}
