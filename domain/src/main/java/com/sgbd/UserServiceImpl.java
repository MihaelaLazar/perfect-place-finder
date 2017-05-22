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
import com.sgbd.repository.EstateRepository;
import com.sgbd.repository.UserRepository;
import com.sgbd.util.SecurityUtil;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import sun.misc.BASE64Decoder;

import javax.crypto.SecretKey;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.sgbd.model.User.USER_EMAIL_COLUMN_NAME;
import static com.sgbd.model.User.USER_ID_COLUMN_NAME;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    private Validator validator;

    @Autowired
    EstateRepository estateRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByAttribute(USER_EMAIL_COLUMN_NAME, email, User.class);
    }

    @Override
    public User findById (Long userId) {
        return userRepository.findByAttribute(USER_ID_COLUMN_NAME, userId, User.class);
    }

    @Override
    public User getUser(String userEmail) {
        return  userRepository.findByAttribute(USER_EMAIL_COLUMN_NAME, userEmail, User.class);
    }

    @Override
    public List<Estate> getUserAnnouncements(Long id) {
        return estateRepository.getUserAnnouncements(id);
    }

    @Override
    public String deleteUser(String userEmail) {
        if (userRepository.findByAttribute(USER_EMAIL_COLUMN_NAME, userEmail,User.class) == null){
            return "user with email " + userEmail +" does not exist";
        } else {
            userRepository.deleteUser(userEmail);
            return "user with email " + userEmail + " deleted";
        }
    }

    @Override
    @Transactional
    public User createUser(SignUpDTO signUpDTO) throws DataIntegrityViolationException,SQLIntegrityConstraintViolationException,EmptyInputException{
        User user = new User();
        String errorMessages = "";
        errorMessages += validateUserFirstName(signUpDTO.getFirstName());
        try {
            errorMessages += validateUserEmail(signUpDTO.getEmail());
        } catch (InvalidRegexException e) {
            errorMessages += e.getMessage();
        }
        errorMessages += validateUserLastName(signUpDTO.getLastName());
        errorMessages += validateUserPassword(signUpDTO.getPassword());
        if (errorMessages.equals("")) {
            user.setFirstName(signUpDTO.getFirstName());
            user.setLastName(signUpDTO.getLastName());
            user.setEmail(signUpDTO.getEmail());
            user.setPassword(signUpDTO.getPassword());
        }
        try {
            String passAndKey[] = SecurityUtil.encryptPassword(user.getPassword());
            user.setPassword(passAndKey[0]);
            user.setKey(passAndKey[1]);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (!errorMessages.equals("")) {
            throw new EmptyInputException(errorMessages);
        }
        return userRepository.createUser(user);
    }

    private String validateUserEmail(String email) throws InvalidRegexException{
        boolean valid = EmailValidator.getInstance().isValid(email);
        if (!valid){
            throw new InvalidRegexException("Invalid email format;");
        }
        return "";

    }

    private String validateUserLastName(String lastName) throws EmptyInputException{
        if (lastName == "" || lastName.length() < 2) {
            return "Invalid lastName;";
        }
        return "";
    }

    private String validateUserFirstName(String firstName) throws EmptyInputException{
        if (firstName == "" || firstName.length() < 2) {
            return  "Invalid firstName;";
        }
        return "";
    }

    private String validateUserPassword(String password) throws EmptyInputException{
        if (password == "" || password.length() < 2) {
            return "Invalid password length;";
        }
        return "";
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws InvalidUserPasswordException, EmptyInputException {
        if (email == "") {
            throw new EmptyInputException("Invalid email;");
        }
        User user = findByEmail(email);
        if (user != null) {
            SecretKey key = SecurityUtil.getSecretKeyFromDB(user.getKey());
            String currentPass = SecurityUtil.bytesToHex(SecurityUtil.encryptText(password, key));
            if (!currentPass.equals(user.getPassword())) {
                throw new InvalidUserPasswordException("Invalid password;");
            }else {
                return user;
            }
        } else {
            return null;
        }

    }

    @Override
    public void setFavoriteAnnouncement(User user, Long idAnnouncement) throws EntityNotFoundException {
        userRepository.addFavoriteAnnouncement(user, idAnnouncement);
    }

    @Override
    public List<Message> getUserMessages(Long id) {
//        User user = userRepository.findByAttribute("id", id, User.class);
        List<Message> messages = new ArrayList<>();
        List<Estate> estates = estateRepository.getUserEstates(id);
        if (estates != null && estates.size() > 0) {
            for(Estate estate: estates) {
                if (estate.getEstateMessages() != null && estate.getEstateMessages().size() > 0) {
                    for(Message estateMessage : estate.getEstateMessages()) {
                        estateMessage.setCreatedAtToString();
                        messages.add(estateMessage);
                        System.out.println(estateMessage.getCreatedAt().toString());
                    }
                }
            }
        }
        return messages;
    }

    @Override
    public User updateUser(UserUpdateDTO userUpdateDTO) throws SQLException, DataIntegrityViolationException{
        User user = userRepository.findByAttribute("id", userUpdateDTO.getIdUser(), User.class);
        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
        try {
            return userRepository.saveOrUpdate(user);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public List<Estate> getUserFavoriteAnnouncements(Long id){
        List<BigDecimal> favoriteAnnouncementsIds = estateRepository.getFavoriteAnnouncementsIds(id);
        List<Estate> favoriteAnnouncements = new LinkedList<>();
        if (favoriteAnnouncementsIds != null) {
            for (BigDecimal idAnnouncement: favoriteAnnouncementsIds) {
                Estate currentEstate = (Estate)estateRepository.findByAttribute("id", idAnnouncement.longValue(), Estate.class);
                if (currentEstate != null) {
                    favoriteAnnouncements.add(currentEstate);
                }
            }
        }
        return favoriteAnnouncements;
    }

    @Override
    public void deleteFavoriteAnnouncement(Long idUser, Long idAnnouncement) {
        userRepository.deleteFavoriteAnnouncement(idUser,idAnnouncement);
    }

    @Override
    public User getLoggedInUser() {
        return this.getUser(getLoggedInUserEmail());
    }

    @Override
    public String getLoggedInUserEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public Long getLoggedInUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = this.findByEmail(getLoggedInUserEmail());
        if (currentUser != null){
            return currentUser.getId();
        }
        else{
            return -1L;
        }
    }

    @Override
    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.getAllUsers();
        List<UserDTO> usersListToReturn = new LinkedList<>();
        for (User user: users) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
            usersListToReturn.add(userDTO);
        }
        return usersListToReturn;
    }

    @Override
    public void deleteUserAccount(Long id) {
        User user = userRepository.findByAttribute("id",id, User.class);
        userRepository.deleteUser(user.getEmail());
    }

    @Override
    @Transactional
    public void updateUserPassword(UserUpdatePasswordDTO userUpdatePassword, Long id) throws SQLException, DataIntegrityViolationException {
        User user = userRepository.findByAttribute("id",id, User.class);
        try {
            String passAndKey[] = SecurityUtil.encryptPassword(userUpdatePassword.getNewPassword());
            user.setPassword(passAndKey[0]);
            user.setKey(passAndKey[1]);
        } catch (Exception e) {
            System.out.println("Could not encrypt password");
        }
        try {
            userRepository.saveOrUpdate(user);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }
}
