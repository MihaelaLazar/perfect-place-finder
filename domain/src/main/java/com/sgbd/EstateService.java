package com.sgbd;

import com.sgbd.dto.PaginatedEstatesDetails;

import java.io.Serializable;

/**
 * Created by mihae on 4/8/2017.
 */
public interface EstateService {
   // PaginatedEstatesDetails getEstatesByFilters(String queryString) throws SQLException, ClassNotFoundException;

    Serializable findById (Long userId);

    PaginatedEstatesDetails getEstatesByFilters(String queryString);
}
