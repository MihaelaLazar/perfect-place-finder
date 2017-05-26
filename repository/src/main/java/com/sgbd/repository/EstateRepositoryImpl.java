package com.sgbd.repository;

import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EstateRepositoryImpl implements EstateRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public Estate findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException {
        final Query query = entityManager.createQuery("SELECT o FROM " + modelClass.getSimpleName()
                + " o WHERE o." + columnName + "=:identifier");
        query.setParameter("identifier", identifier);

        final List<Serializable> results = query.getResultList();

        if (!results.isEmpty()) {
            return (Estate)results.iterator().next();
        }

        return null;
    }

    @Override
    @Transactional
    public List<Estate> getUserEstates(Serializable id) {
        final Query query = entityManager.createQuery("SELECT o FROM " + Estate.class.getSimpleName()
                + " o WHERE o.idUser =:identifier");
        query.setParameter("identifier", id);
        final List<Estate> results = query.getResultList();
        return results;
    }

    @Override
    public List<BigDecimal> getFavoriteAnnouncementsIds(Long id) {
        Query query = entityManager.createNativeQuery("SELECT o.ID_ANNOUNCEMENT FROM PF_FAV_ANNOUNCEMENTS o WHERE o.ID_USER = " + id);
        final List<BigDecimal> estatesIds = query.getResultList();
        return estatesIds;
    }


    @Override
    @Transactional
    public PaginatedEstatesDetails getEstatesByFilters(String queryFilters, Integer offset){
        List estates = entityManager.createQuery("from Estate WHERE " + queryFilters + " ORDER BY ID").setFirstResult(offset).setMaxResults(10).getResultList();
        PaginatedEstatesDetails paginatedEstatesDetails = new PaginatedEstatesDetails();
        paginatedEstatesDetails.setEstates(estates);
        paginatedEstatesDetails.setTotalCount( entityManager.createQuery("from Estate WHERE " + queryFilters ).getResultList().size());
        paginatedEstatesDetails.setOffset(offset);
        return paginatedEstatesDetails;
    }


    @Transactional
    public Serializable save(Serializable entity, Class modelClass){
             entityManager.persist(entity);
             return entity;

    }

    @Override
    @Transactional
    public Estate saveOrUpdate(Estate estate) {
        return entityManager.merge(estate);
    }

    @Override
    @Transactional
    public List<Estate> getUserAnnouncements(Long id) {
        final Query query = entityManager.createQuery(" FROM " + Estate.class.getSimpleName()
                + " WHERE " + "idUser" + " =:identifier");
        query.setParameter("identifier", id);

        List<Estate> estates = (List<Estate>) query.getResultList();
        return estates;
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Serializable entity, Class modelClass) {
        Estate estate = (Estate)entity;
        entityManager.createNativeQuery("DELETE FROM PF_ANNOUNCEMENTS WHERE  ID = " + estate.getID().toString())
                .executeUpdate();
    }

    @Override
    @Transactional
    public List<Estate> getAllEstates() {
        Query query = entityManager.createQuery("from Estate ");
        List<Estate> estates = (List<Estate>) query.getResultList();
        return estates;
    }

    @Override
    @Transactional
    public List<Message> getAllMessages(){
        Query query = entityManager.createQuery("from Message ");
        List<Message> messages = (List<Message>) query.getResultList();
        return messages;
    }
}
