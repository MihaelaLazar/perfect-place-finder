package com.sgbd.repository;

import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.model.Estate;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by mihae on 4/8/2017.
 */
public interface EstateRepository {

    Serializable findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException;

    PaginatedEstatesDetails getEstatesByFilters(String queryString, Integer offset);

    Serializable save(Serializable entity, Class modelClass) throws PersistenceException;

    Estate saveOrUpdate(Estate estate) throws PersistenceException;

    List<Estate> getUserAnnouncements(Long id);
}
