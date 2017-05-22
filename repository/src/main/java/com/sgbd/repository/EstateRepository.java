package com.sgbd.repository;

import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by mihae on 4/8/2017.
 */
public interface EstateRepository {

    Serializable findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException;

    PaginatedEstatesDetails getEstatesByFilters(String queryString, Integer offset);

    Serializable save(Serializable entity, Class modelClass) throws PersistenceException;

    Estate saveOrUpdate(Estate estate);

    List<Estate> getUserAnnouncements(Long id);

//    List<Estate> getUserFavouriteAnnouncements(Long id);

    void deleteAnnouncement(Serializable entity, Class modelClass);

    List<Estate> getUserEstates(Serializable id);

    List<BigDecimal> getFavoriteAnnouncementsIds(Long id);

    List<Estate> getAllEstates();

    List<Message> getAllMessages();
}
