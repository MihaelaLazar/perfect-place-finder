package com.sgbd.controller;


import com.sgbd.OracleCon;
import com.sgbd.UserService;
import com.sgbd.model.User;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by mihae on 4/3/2017.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/user/register", method = RequestMethod.GET)
    public Object register(Request request,Response response) {
        System.out.println("register");
        String[] myJsonData = request.getParameterValues("json");
        System.out.println(myJsonData);
        try {
            response.sendRedirect("/homePage.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";
    }

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
            response.sendRedirect("/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";
    }

    @RequestMapping(path = "/create/user", method = RequestMethod.POST)
    public Object addUser(Response response){
        try {
            response.sendRedirect("/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";
    }

    @RequestMapping(path = "/create/user", method = RequestMethod.GET)
    public Object addUserGET(Request request,Response response){
        System.out.println(request.getAttributeNames().toString().charAt(0));
        System.out.println("in addUserGet()");
        try {
            response.sendRedirect("/estateDetails.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";
    }

    @RequestMapping(path = "/addPerson", method = RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<String>  addPerson(Request request,Response response, @RequestBody String postData) {
        System.out.println("ADD PERSON");
        //System.out.println(request.getHttpFields());
        //System.out.println(postData);
        String postDataCopy = "";
        for (int index = 1; index < postData.length() -1; index++) {
            postDataCopy += postData.charAt(index);
        }
        String [] fields = postDataCopy.split(",");
        OracleCon oracleCon = new OracleCon();
        try {
            oracleCon.addUser(fields);
        } catch (SQLException e) {
            return new ResponseEntity<String>("DUPLICATE",HttpStatus.FORBIDDEN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        response.setContentType("application/json");
        return new ResponseEntity<String>("{'name': 'Mihaela', 'password':'asd'}",HttpStatus.OK);
    }

        @RequestMapping(value="/create", method=RequestMethod.POST,
                produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public User createUser(@RequestBody User user) {
            return userService.createUser(user);
        }



    @RequestMapping(path = "/addProperty", method = RequestMethod.GET)
    public @ResponseBody String getAddProperty(Response response) {
        System.out.println();
        response.setContentType("text/html");

        try {
            response.sendRedirect("/addPropertyRaluca.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
