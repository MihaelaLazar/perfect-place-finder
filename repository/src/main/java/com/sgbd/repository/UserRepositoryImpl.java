package com.sgbd.repository;

import com.sgbd.model.Estate;
import com.sgbd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
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

    @Autowired
    EstateRepository estateRepository;

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
        entityManager.persist(entity);
        return entity;

    }

    @Override
    public User saveOrUpdate(User user) {
        entityManager.merge(user);
        return user;
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

    @Override
    @Transactional
    public void addFavoriteAnnouncement(User user, Long idAnnouncement) throws EntityNotFoundException {
        Estate estate = (Estate) estateRepository.findByAttribute(ESTATE_ID_COLUMN_NAME, idAnnouncement ,Estate.class);
        if (estate != null) {
            entityManager.createNativeQuery("INSERT INTO PF_FAV_ANNOUNCEMENTS VALUES(?,?)")
                    .setParameter(1, user.getId())
                    .setParameter(2,idAnnouncement)
                    .executeUpdate();
        } else {
            throw new EntityNotFoundException("Announcement not found");
        }
    }

    @Override
    @Transactional
    public void deleteFavoriteAnnouncement(Long idUser, Long idAnnouncement) {
        entityManager.createNativeQuery("DELETE FROM PF_FAV_ANNOUNCEMENTS WHERE ID_USER = " + idUser + " AND ID_ANNOUNCEMENT = " + idAnnouncement)
                .executeUpdate();
    }
}
