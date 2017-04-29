package com.sgbd.controller;

import com.sgbd.OracleCon;
import com.sgbd.UserService;
import com.sgbd.dto.LoginDTO;
import com.sgbd.dto.SignUpDTO;
import com.sgbd.model.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static com.sgbd.util.ContentType.JSON;

@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Object redirectToHomePage(Request request, Response response) {
        response.setContentType("text/html");

        try {
            response.sendRedirect("/homePage.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public Object error(Response response) {
        try {
            response.sendRedirect("/error.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addPerson(Request request, Response response, @RequestBody SignUpDTO user) {

//        try {
//            OracleCon.getOracleCon().addUser(user);
//        } catch (SQLException e) {
//            return new ResponseEntity<>("DUPLICATE", HttpStatus.FORBIDDEN);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        response.setContentType(JSON.getContentType());
//        return new ResponseEntity<>("Added in database", HttpStatus.OK);

        try {
            userService.createUser(user);
            response.setContentType(JSON.getContentType());
            return new ResponseEntity<>("Added in database", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            response.setContentType(JSON.getContentType());
            return new ResponseEntity<>("DUPLICATE", HttpStatus.CONFLICT);
        } catch (SQLIntegrityConstraintViolationException e) {
            response.setContentType(JSON.getContentType());
            return new ResponseEntity<>("DUPLICATE", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> validateUser(Response response, Request request, @RequestBody LoginDTO loginDTO) {
//        String[] emailAndPassword;
//
//        try {
//            emailAndPassword = OracleCon.getOracleCon().validateUser(loginDTO);
//        } catch (SQLException e) {
//            return new ResponseEntity<>("INVALID USER/PASSWORD", HttpStatus.FORBIDDEN);
//        } catch (ClassNotFoundException e) {
//            return new ResponseEntity<>("THERE WAS A INTERNAL PROBLEM. PLEASE TRY AGAIN LATER !", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        HttpSession session = request.getSession();
//        session.setAttribute("Username", emailAndPassword[0]);
//        session.setAttribute("Password", emailAndPassword[1]);
//
//        return new ResponseEntity<>("Login succeed", HttpStatus.OK);

        String[] emailAndPassword;
        try {
            User user = (User) userService.findByEmailAndPassword(loginDTO.getEmail(),loginDTO.getPassword());
            if (user != null) {
                return new ResponseEntity<>("Login succeed", HttpStatus.OK);
            }else {
                return new ResponseEntity<String>("INVALID EMAIL/PASSWORD", HttpStatus.CONFLICT);
            }

        }catch (EntityNotFoundException e){
            return new ResponseEntity<String>("EMAIL NOT FOUND", HttpStatus.FORBIDDEN);
        }
    }


}
