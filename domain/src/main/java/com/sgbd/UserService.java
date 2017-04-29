package com.sgbd;

import com.sgbd.dto.SignUpDTO;
import com.sgbd.model.User;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by mihae on 4/3/2017.
 */
public interface UserService {
    Serializable findByEmail(String email);

    User getUser(String userEmail);

    String deleteUser(String userEmail);

    Serializable findById (Long userId);

    User createUser(SignUpDTO user) throws SQLIntegrityConstraintViolationException;

    User findByEmailAndPassword(String email, String password);
}
