package com.sgbd.repository;

import com.sgbd.dto.PaginatedEstatesDetails;

import javax.persistence.PersistenceException;
import java.io.Serializable;

/**
 * Created by mihae on 4/8/2017.
 */
public interface EstateRepository {

     Serializable findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException;

    PaginatedEstatesDetails getEstatesByFilters(String queryString, Integer offset);
}
