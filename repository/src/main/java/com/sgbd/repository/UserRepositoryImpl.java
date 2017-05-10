package com.sgbd.repository;

import com.sgbd.model.Estate;
import com.sgbd.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static com.sgbd.model.Estate.ESTATE_ID_COLUMN_NAME;
import static com.sgbd.model.User.USER_EMAIL_COLUMN_NAME;

/**
 * Created by mihae on 4/3/2017.
 */

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public User findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException {
        final Query query = entityManager.createQuery("SELECT o FROM " + modelClass.getSimpleName()
                + " o WHERE o." + columnName + "=:identifier");
        query.setParameter("identifier", identifier);
//        entityManager.find(User.class,identifier);
        final List<User> results = query.getResultList();

        if (!results.isEmpty()) {
            return results.iterator().next();
        }

        return null;
    }

    @Override
    @Transactional
    public User createUser(User user) throws DataIntegrityViolationException,SQLIntegrityConstraintViolationException {
        entityManager.persist(user);
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public Serializable save(Serializable entity, Class modelClass) throws PersistenceException {
        try {
            return entityManager.merge(entity);
        } catch (Exception e) {
            throw new PersistenceException("Failed saveOrUpdate for entity class " + modelClass, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void deleteUser(String userEmail) {
        try {
            entityManager.remove(findByAttribute(USER_EMAIL_COLUMN_NAME, userEmail, User.class));
        }catch (Exception e) {
            throw new PersistenceException("Failed delete for entity class " + User.class, e);
        }
    }

    @Override
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

//    @Override
//    @Transactional
//    public User addFavoriteAnnouncement(User user, Long idAnnouncement) {
//        Estate estate = (Estate) findByAttribute(ESTATE_ID_COLUMN_NAME, idAnnouncement ,Estate.class);
//        user.getFavoriteAnnouncements().add(estate);
//        entityManager.merge(user);
//        return user;
//    }
}
