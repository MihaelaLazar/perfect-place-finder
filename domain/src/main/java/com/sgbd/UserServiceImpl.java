package com.sgbd;

import com.sgbd.model.User;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by mihae on 4/3/2017.
 */

@Service
public class UserServiceImpl implements UserService{

    @Override
    public Serializable findByEmail(String email) {
        return null;
    }

    @Override
    public User getUser(String userEmail) {
        return new User();
    }

    @Override
    public String deleteUser(String userEmail) {
        return null;
    }

    @Override
    public Serializable findById(Long userId) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return user;
    }
}
