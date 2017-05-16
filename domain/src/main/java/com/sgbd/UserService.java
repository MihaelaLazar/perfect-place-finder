package com.sgbd;

import com.sgbd.dto.SignUpDTO;
import com.sgbd.dto.UserUpdateDTO;
import com.sgbd.exceptions.EmptyInputException;
import com.sgbd.exceptions.InvalidRegexException;
import com.sgbd.exceptions.InvalidUserPasswordException;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.model.User;

import javax.persistence.EntityNotFoundException;
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

    User createUser(SignUpDTO user) throws SQLIntegrityConstraintViolationException, EmptyInputException;

    User findByEmailAndPassword(String email, String password) throws InvalidUserPasswordException;

    List<Estate> getUserAnnouncements(Long id);

    void setFavoriteAnnouncement(User user, Long idAnnouncement) throws EntityNotFoundException;

    List<Message> getUserMessages(Long id);

    User updateUser(UserUpdateDTO updateDTO);

    List<Estate> getUserFavoriteAnnouncements(Long id);

    void deleteFavoriteAnnouncement(Long idUser, Long idAnnouncement);

    User getLoggedInUser();

    String getLoggedInUserEmail();

    Long getLoggedInUserId();
}
