package com.sgbd.repository;

import com.sgbd.model.User;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Created by mihae on 4/3/2017.
 */
public interface UserRepository {

    User findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException;

    User saveOrUpdate(User user) throws SQLException, DataIntegrityViolationException;

    void deleteUser(String userEmail);

    User createUser(User user) throws DataIntegrityViolationException,SQLIntegrityConstraintViolationException;

    Serializable save(Serializable entity, Class modelClass) throws PersistenceException;

    void close();

    void addFavoriteAnnouncement(User user, Long idAnnouncement) throws EntityNotFoundException;

    void deleteFavoriteAnnouncement(Long idUser, Long idAnnouncement);

    List<User> getAllUsers();
}
