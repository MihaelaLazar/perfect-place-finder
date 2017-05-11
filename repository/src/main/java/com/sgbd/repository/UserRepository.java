package com.sgbd.repository;

import com.sgbd.model.User;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by mihae on 4/3/2017.
 */
public interface UserRepository {

    User findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException;

    User saveOrUpdate(User user);

    void deleteUser(String userEmail);

    User createUser(User user) throws DataIntegrityViolationException,SQLIntegrityConstraintViolationException;

    Serializable save(Serializable entity, Class modelClass) throws PersistenceException;

    void close();

//    User addFavoriteAnnouncement(User user, Long idAnnouncement);
}
