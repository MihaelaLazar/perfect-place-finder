package com.sgbd;

import com.sgbd.dto.SignUpDTO;
import com.sgbd.dto.UpdateUserDTO;
import com.sgbd.model.Estate;
import com.sgbd.model.User;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Created by mihae on 4/3/2017.
 */
public interface UserService {
    User findByEmail(String email);

    User getUser(String userEmail);

    String deleteUser(String userEmail);

    User findById (Long userId);

    User createUser(SignUpDTO user) throws SQLIntegrityConstraintViolationException;

    User findByEmailAndPassword(String email, String password);

    List<Estate> getUserAnnouncements(Long id);

    User setFavoriteAnnouncement(User user, Long idAnnouncement);

    //User updateUser(UpdateUserDTO updateDTO);
}
