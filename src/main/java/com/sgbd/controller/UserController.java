package com.sgbd.controller;

import com.sgbd.UserService;
import com.sgbd.dto.LoginDTO;
import com.sgbd.dto.SignUpDTO;
import com.sgbd.dto.UserDTO;
import com.sgbd.dto.UserUpdateDTO;
import com.sgbd.exceptions.EmptyInputException;
import com.sgbd.exceptions.InvalidUserPasswordException;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.model.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import static com.sgbd.util.ContentType.JSON;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Object redirectToHomePage(Request request, Response response) {
        response.setContentType("text/html");
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        System.out.println("TOKEN: " + token);
        HttpSession httpSession = request.getSession(true);
        httpSession.setMaxInactiveInterval(2);
        httpSession.setAttribute("token",token);
        System.out.println("Last accessed time: " + httpSession.getLastAccessedTime());
        Cookie cookie = new Cookie("token", token);
        request.setCookies(new Cookie[]{cookie});
        response.addCookie(cookie);
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


    @RequestMapping(path = "/create/user", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addPerson(Request request, Response response, @RequestBody SignUpDTO user) {
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
        }catch (EmptyInputException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/verify/user", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> validateUser(Response response, Request request, @RequestBody LoginDTO loginDTO) {
        HttpSession session = request.getSession(true);
        try {
            User user =  userService.findByEmailAndPassword(loginDTO.getEmail(),loginDTO.getPassword());
            String tokenID = session.getId();
            session.setAttribute("username",user.getUsername());
            session.setAttribute("tokenID", tokenID);
            session.setMaxInactiveInterval(20 * 60);
            request.getSession(false).setAttribute("username", user.getUsername());
            request.getSession(false).setAttribute("tokenID", tokenID);
            request.getSession(false).setAttribute("ID", user.getId());
            request.getSession(false).setAttribute("admin", "admin");
            if (user.getRole().equalsIgnoreCase("admin")){
                return new ResponseEntity<>("admin", HttpStatus.OK);
            }
            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);

        }catch (EntityNotFoundException e){
            session.removeAttribute("username");
            session.removeAttribute("tokenID");
            session.removeAttribute("admin");
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
            return new ResponseEntity<>("EMAIL NOT FOUND", HttpStatus.FORBIDDEN);
        }catch (EmptyInputException e) {
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (InvalidUserPasswordException e) {
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/user/getProfile", method = RequestMethod.GET)
    public ResponseEntity<String> redirectToProfile(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        if (session != null) {
//                response.sendRedirect("/profile.html");
            if (session.getAttribute("admin") != null){
                return new ResponseEntity<>("/adminProfile.html", HttpStatus.OK);
            }
            return new ResponseEntity<>("/profile.html", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Session not existent", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/user/check/session", method = RequestMethod.GET)
    public ResponseEntity<String> checkSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            return new ResponseEntity<>((String) session.getAttribute("username"),HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not existent",HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/user/logout", method = RequestMethod.GET)
    public String invalidateSession (HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return "invalidated";
    }

    @RequestMapping(path = "/user/addProperty", method = RequestMethod.GET)
    public ResponseEntity<String> redirectToAddProperty (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return new ResponseEntity<>("/addProperty.html", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Session not existent", HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(path = "/user/getAnnouncements", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Estate>> getAnnouncements(Response response, Request request) {
        HttpSession session = request.getSession();
        Long id = (Long) session.getAttribute("ID");
        List<Estate> userAnnouncements = null;
        try {
            User user =  userService.findById(id);
            userAnnouncements = userService.getUserAnnouncements(id);
            return new ResponseEntity<>(userAnnouncements, HttpStatus.OK);

        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(userAnnouncements, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user/add/favoriteAnnouncement", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addFavoriteAnnouncement(Response response, Request request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Long id = (Long) session.getAttribute("ID");
            User user;
            try {
                user = userService.findById(id);
                userService.setFavoriteAnnouncement(user,Long.parseLong(request.getParameter("idAnnouncement")));
                return new ResponseEntity<>("Announcement added to favorites", HttpStatus.OK);
            }catch (EntityNotFoundException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not logged in", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user/delete/favoriteAnnouncement", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteFavoriteAnnouncement(Response response, Request request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Long id = (Long) session.getAttribute("ID");
            User user;
            try {
                userService.deleteFavoriteAnnouncement(id,Long.parseLong(request.getParameter("idAnnouncement")));
                return new ResponseEntity<>("Announcement deleted from favorites", HttpStatus.OK);
            }catch (EntityNotFoundException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not logged in", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user/get/favoriteAnnouncements", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Estate>> getUserFavoriteAnnouncements(Response response, Request request){
        HttpSession session = request.getSession();
        List<Estate> favoriteAnnouncements = new LinkedList<>();
        if (session != null) {
            Long id = (Long) session.getAttribute("ID");
            favoriteAnnouncements = userService.getUserFavoriteAnnouncements(id);
            return new ResponseEntity<>(favoriteAnnouncements, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(favoriteAnnouncements, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user/get/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessages (Response response, Request request) {
        List<Message> messages = new ArrayList<>();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        } else {
            Long id = (Long) session.getAttribute("ID");
            messages = userService.getUserMessages(id);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/update/profile", method = RequestMethod.POST)
    public ResponseEntity<String>  updateProfile (Request request, Response response, @RequestBody UserUpdateDTO userUpdateDTO ) {
      userService.updateUser(userUpdateDTO);
      return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAllUsers (Request request, Response response) {
        List<UserDTO> users = new LinkedList<>();
        users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/deleteAccount", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUserAccount(Request request, Response response){
        userService.deleteUserAccount(Long.parseLong(request.getParameter("id")));
        return new ResponseEntity<String>("User deleted", HttpStatus.OK);
    }

}
