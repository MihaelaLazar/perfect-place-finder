package com.sgbd.controller;

import com.sgbd.DTO.LoginDTO;
import com.sgbd.DTO.SignUpDTO;
import com.sgbd.OracleCon;
import com.sgbd.UserService;
import com.sgbd.util.ContentType;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static com.sgbd.util.ContentType.JSON;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Object apasa(Request request, Response response) {
        System.out.println("Redirect in homePage");
        response.setContentType("text/html");

        try {
            response.sendRedirect("/homePage.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public Object error(Response response){
        try {
            response.sendRedirect("/error.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";
    }



    @RequestMapping(path = "/create/user", method = RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<String>  addPerson(Request request,Response response, @RequestBody SignUpDTO user) {
        System.out.println("ADD USER");
        //System.out.println(request.getHttpFields());
        System.out.println(user);
        try {
            OracleCon.getOracleCon().addUser(user);
        } catch (SQLException e) {
            return new ResponseEntity<>("DUPLICATE",HttpStatus.FORBIDDEN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        response.setContentType(JSON.getContentType());
        return new ResponseEntity<>("Added in database",HttpStatus.OK);
    }

    @RequestMapping(path = "/verify/user", method = RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<String>  validateUser(Response response,Request request,@RequestBody LoginDTO loginDTO) {
        System.out.println("VALIDATE USER");
        System.out.println(loginDTO.toString());
        String[] emailAndPassword = new String[3];
        try {
            emailAndPassword =  OracleCon.getOracleCon().validateUser(loginDTO);
        } catch (SQLException e) {
            return new ResponseEntity<>("INVALID USER/PASSWORD",HttpStatus.FORBIDDEN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HttpSession session = request.getSession();
        session.setAttribute("Username", emailAndPassword[0]);
        session.setAttribute("Password", emailAndPassword[1]);

//        Cookie []cookie = request.getCookies();
//        System.out.println("Cookies: " + cookie[0].getName());
        System.out.println(session.getCreationTime() + " " + session.getAttribute("Username"));
        if (session.getAttribute("Password") == null){
            System.out.println("attribute non-existent");
        }
        return new ResponseEntity<>("Login succeed", HttpStatus.OK);
    }



}
