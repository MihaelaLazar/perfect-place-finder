package com.sgbd;

import com.sgbd.dto.SignUpDTO;
import com.sgbd.dto.UserUpdateDTO;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.model.User;
import com.sgbd.repository.EstateRepository;
import com.sgbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


import static com.sgbd.model.User.USER_EMAIL_COLUMN_NAME;
import static com.sgbd.model.User.USER_ID_COLUMN_NAME;

/**
 * Created by mihae on 4/3/2017.
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

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
        return (User) userRepository.findByAttribute(USER_EMAIL_COLUMN_NAME, userEmail, User.class);
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
    public User createUser(SignUpDTO signUpDTO) throws DataIntegrityViolationException,SQLIntegrityConstraintViolationException{
        User user = new User();
        System.out.println("FIRST NAME: " + signUpDTO.getFirstName());
        user.setFirstName(signUpDTO.getFirstName());
        user.setLastName(signUpDTO.getLastName());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(signUpDTO.getPassword());
        return userRepository.createUser(user);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = (User) findByEmail(email);
        if (!user.getPassword().equals(password)) {
            return null;
        }else {
            return user;
        }
    }

    @Override
    public User setFavoriteAnnouncement(User user, Long idAnnouncement) {
         return  userRepository.addFavoriteAnnouncement(user, idAnnouncement);
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
    public User updateUser(UserUpdateDTO updateDTO) {
        User user = userRepository.findByAttribute("id", updateDTO.getIdUser(), User.class);
        user.setFirstName(updateDTO.getFirstName());
        user.setLastName(updateDTO.getLastName());
        user.setEmail(updateDTO.getEmail());

        user=userRepository.saveOrUpdate(user);
        return userRepository.saveOrUpdate(user);
    }

}
