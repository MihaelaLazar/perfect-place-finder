package com.sgbd;


import com.sgbd.model.User;
import com.sgbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cosmin on 8/3/2017.
 */

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.sgbd.model.User user = userRepository.findByAttribute("email",email, User.class);

        if (user == null){
            throw new UsernameNotFoundException("Email not found");
        }
//        THIS SHOULD GET AUTHORITIES OR STH
//        List<GrantedAuthority> authorities = user.getAuthorities();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER_ROLE"));
        authorities.add(new SimpleGrantedAuthority("ADMIN_ROLE"));

        // Converts com.sgbd.model.User to org.springframework.security.core.userdetails.User
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
