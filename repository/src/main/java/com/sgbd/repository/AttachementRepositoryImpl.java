package com.sgbd.repository;

import com.sgbd.model.Attachement;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class AttachementRepositoryImpl implements AttachementRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void delete(Attachement attachement) {
        entityManager.remove(attachement);
    }
}
