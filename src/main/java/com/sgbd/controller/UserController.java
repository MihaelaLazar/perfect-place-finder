package com.sgbd.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sgbd.UserService;
import com.sgbd.dto.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sgbd.util.ContentType.JSON;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

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
            if (user != null) {
                String tokenID = session.getId();
                session.setAttribute("username",user.getUsername());
                session.setAttribute("tokenID", tokenID);
                session.setMaxInactiveInterval(20 * 60);
                request.getSession(false).setAttribute("username", user.getUsername());
                request.getSession(false).setAttribute("tokenID", tokenID);
                request.getSession(false).setAttribute("ID", user.getId());
                if (user.getRole().equalsIgnoreCase("admin")){
                    request.getSession(false).setAttribute("username", "admin");
                    return new ResponseEntity<>("admin", HttpStatus.OK);
                }
                return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("EMAIL NOT FOUND", HttpStatus.FORBIDDEN);
            }
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
            try {
                response.sendRedirect("/searchCity.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            try {
                response.sendRedirect("/searchCity.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            try {
                response.sendRedirect("/homePage.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(favoriteAnnouncements, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user/get/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessages (Response response, Request request) {
        List<Message> messages = new ArrayList<>();
        HttpSession session = request.getSession(false);
        if (session == null) {
            try {
                response.sendRedirect("/homePage.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        } else {
            Long id = (Long) session.getAttribute("ID");
            messages = userService.getUserMessages(id);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/update/profile", method = RequestMethod.POST)
    public ResponseEntity<String>  updateProfile (Request request, Response response, @RequestBody UserUpdateDTO userUpdateDTO ) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (userUpdateDTO.getEmail().equals("")) {
                return new ResponseEntity<>("email_null", HttpStatus.BAD_REQUEST);
            }
            Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(userUpdateDTO.getEmail());
            if (!matcher.find()) {
                return new ResponseEntity<>("email_invalid", HttpStatus.BAD_REQUEST);
            }
            userUpdateDTO.setIdUser((Long) session.getAttribute("ID"));
            userService.updateUser(userUpdateDTO);
        }

        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/update/password", method = RequestMethod.POST)
    public ResponseEntity<String> updatePassword(Request request, Response response, @RequestBody UserUpdatePasswordDTO userUpdatePassword){
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (userUpdatePassword.getNewPassword().equals("") ) {
                return new ResponseEntity<>("password_null", HttpStatus.BAD_REQUEST);
            }
            if (userUpdatePassword.getUserPasswordConfirmed().equals("") ) {
                return new ResponseEntity<>("password_confirmed_null", HttpStatus.BAD_REQUEST);
            }
            if (!userUpdatePassword.getUserPasswordConfirmed().equals(userUpdatePassword.getNewPassword()) ) {
                return new ResponseEntity<>("password_confirmed_not_equal", HttpStatus.BAD_REQUEST);
            }
            userService.updateUserPassword(userUpdatePassword, (Long) session.getAttribute("ID"));
        }
        return new ResponseEntity<>("invalid session", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/get/profileAccount", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUSerProfileAccount(Request request, Response response) {
        UserDTO userDTO = new UserDTO();
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = userService.getUser((String) session.getAttribute("username"));
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstname(user.getFirstName());
            userDTO.setLastname(user.getLastName());
            userDTO.setId(user.getId());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }else {
            try {
                response.sendRedirect("/homePage.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(userDTO, HttpStatus.CONFLICT);
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
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }


}
