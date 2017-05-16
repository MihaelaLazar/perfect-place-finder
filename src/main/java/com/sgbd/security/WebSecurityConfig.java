//package com.sgbd.security;
//
///**
// * Created by cosmin on 5/7/17.
// */
//
//
//import com.sgbd.LoginService;
//import com.sgbd.UserService;
//import com.sgbd.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@ComponentScan("com.sgbd")
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//    // To allow full access to everyone on all pages, set this to true.
//    private static final boolean ALL_ACCESS_ALLOWED = true;
//
//    // To print Spring Security debug output, set this to true
//    private static final boolean DEBUG_VALUE = false;
//
//    @Autowired
//    @Qualifier("userDetailsService")
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        /*
//          Edit this to allow unrestricted access to certain pages.
//          Can be overwritten by ALL_ACCESS_ALLOWED.
//         */
//        String[] allowed = {"/login", "/paginacurs", "/judet"};
//
//        if (ALL_ACCESS_ALLOWED){
//            allowed = new String[]{"/**"};
//        }
//
//        http
//                .authorizeRequests()
//                    .antMatchers(allowed).permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                    .loginPage("/login")
//                    .defaultSuccessUrl("/homePage.html", true)
//                    .usernameParameter("email")
//                    .passwordParameter("password")
//                    .permitAll()
//                .and()
//                .logout()
//                    .permitAll()
//                .and()
//                .csrf().disable();
//    }
//
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.debug(DEBUG_VALUE);
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(this.userDetailsService)
//                .passwordEncoder(getPasswordEncoder());
//    }
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * Enables loginService on every HTML page
//     */
//    @Bean(name = "loginService")
//    public LoginService loginService() {
//
//        return new LoginService() {
//            @Override
//            public boolean isLoggedIn() {
//                // anonymousUser is the returned email if user is not logged in
//                return !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
//            }
//
//            @Override
//            public String getEmail() {
//                // if logged out, returns "anonymousUser"
//                return SecurityContextHolder.getContext().getAuthentication().getName();
//            }
//
//            @Override
//            public Long getId() {
//                User user = userService.getUser(getEmail());
//                return (user == null ? -1 : user.getId());
//            }
//        };
//    }
//}
//
