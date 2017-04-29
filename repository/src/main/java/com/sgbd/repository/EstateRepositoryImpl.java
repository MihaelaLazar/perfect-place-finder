package com.sgbd.repository;

import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by mihae on 4/8/2017.
 */
@Repository
@Transactional
public class EstateRepositoryImpl implements EstateRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public Serializable findByAttribute(String columnName, Serializable identifier, Class modelClass) throws PersistenceException {
        final Query query = entityManager.createQuery("SELECT o FROM " + modelClass.getSimpleName()
                + " o WHERE o." + columnName + "=:identifier");
        query.setParameter("identifier", identifier);

        final List<User> results = query.getResultList();

        if (!results.isEmpty()) {
            return results.iterator().next();
        }

        return null;
    }

    @Override
    @Transactional
    public PaginatedEstatesDetails getEstatesByFilters(String queryFilters, Integer offset){
        List estates = entityManager.createQuery("from Estate WHERE " + queryFilters).setFirstResult(offset).setMaxResults(offset + 10).getResultList();
        PaginatedEstatesDetails paginatedEstatesDetails = new PaginatedEstatesDetails();
        paginatedEstatesDetails.setEstates(estates);
        paginatedEstatesDetails.setTotalCount( entityManager.createQuery("from Estate WHERE " + queryFilters).getResultList().size());
        paginatedEstatesDetails.setOffset(offset);
        return paginatedEstatesDetails;
    }
}
