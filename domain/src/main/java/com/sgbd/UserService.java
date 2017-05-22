package com.sgbd;

import com.sgbd.dto.SignUpDTO;
import com.sgbd.dto.UserDTO;
import com.sgbd.dto.UserUpdateDTO;
import com.sgbd.dto.UserUpdatePasswordDTO;
import com.sgbd.exceptions.EmptyInputException;
import com.sgbd.exceptions.InvalidRegexException;
import com.sgbd.exceptions.InvalidUserPasswordException;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
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

    User findByEmailAndPassword(String email, String password) throws InvalidUserPasswordException, EmptyInputException;

    List<Estate> getUserAnnouncements(Long id);

    void setFavoriteAnnouncement(User user, Long idAnnouncement) throws EntityNotFoundException;

    List<Message> getUserMessages(Long id);

    User updateUser(UserUpdateDTO updateDTO) throws SQLException, DataIntegrityViolationException;

    List<Estate> getUserFavoriteAnnouncements(Long id);

    void deleteFavoriteAnnouncement(Long idUser, Long idAnnouncement);

    User getLoggedInUser();

    String getLoggedInUserEmail();

    Long getLoggedInUserId();

    List<UserDTO> getAllUsers();

    void deleteUserAccount(Long id);

    void updateUserPassword(UserUpdatePasswordDTO userUpdatePassword, Long id) throws SQLException;
}
