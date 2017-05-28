package com.sgbd.controller;

import com.sgbd.UserService;
import com.sgbd.dto.*;
import com.sgbd.exceptions.EmptyInputException;
import com.sgbd.exceptions.InvalidUserPasswordException;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
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
    @ApiOperation(value = "This endpoint creates a new user (sign-in).", nickname = "signin", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 409, message = "Unauthorized - already existent user with same email."),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> signin(Request request, Response response, @RequestBody SignUpDTO user) {
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
    @ApiOperation(value = "This endpoint logs an user (log-in).", nickname = "login", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - logged in", response = String.class),
            @ApiResponse(code = 403, message = "Forbidden - email not found/incorrect password."),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint redirects user to his profile.", nickname = "getProfile", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 409, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> redirectToProfile(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        if (session != null) {
//                response.sendRedirect("/profile.html");
            if (session.getAttribute("username").equals("admin")){
                return new ResponseEntity<>("/adminProfile.html", HttpStatus.OK);
            }
            return new ResponseEntity<>("/profile.html", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Session not existent", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/user/check/session", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint checks if session is existent.", nickname = "session", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - session existent.", response = String.class),
            @ApiResponse(code = 409, message = "Session not existent."),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint invalidates session.", nickname = "signin", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - session invalidated.", response = String.class),
            @ApiResponse(code = 500, message = "Failure")})
    public String invalidateSession (HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return "invalidated";
    }

    @RequestMapping(path = "/user/addProperty", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint redirects to addProperty.html page.", nickname = "redirectAddProperty", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint returns a list of user announcements.", nickname = "getAnnouncements", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint creates a favorite user announcement.", nickname = "addFavAnnouncement", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - fav announcement added.", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint deletes a favorite user announcement.", nickname = "deleteFavAnnouncement", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - fav announcement deleted.", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint returns a list with favorite user announcements.", nickname = "getAllFavAnnouncement", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - fav announcements returned.", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint returns user's messages.", nickname = "getUserMesages", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - messages returned.", response = String.class),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint updates user's profile(email).", nickname = "updateEmail", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Success - email updated.", response = String.class),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
            try {
                userService.updateUser(userUpdateDTO);
            } catch (SQLException e) {
                System.out.println("here sql exception");
                return new ResponseEntity<>("Update could not be completed.", HttpStatus.BAD_REQUEST);
            }catch (DataIntegrityViolationException e) {
                return new ResponseEntity<>("Update could not be completed.", HttpStatus.BAD_REQUEST);
            }

            session.removeAttribute("username");
            session.setAttribute("username", userUpdateDTO.getEmail());
        }

        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/update/password", method = RequestMethod.POST)
    @ApiOperation(value = "This endpoint updates user's password.", nickname = "updatePassword", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Success - fav announcement added.", response = String.class),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
            try {
                userService.updateUserPassword(userUpdatePassword, (Long) session.getAttribute("ID"));
            } catch (SQLException e) {
                return new ResponseEntity<>("Update password could not be completed.", HttpStatus.BAD_REQUEST);
            }catch (DataIntegrityViolationException e) {
                return new ResponseEntity<>("Update password could not be completed.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("invalid session", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/get/profileAccount", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint returns users's profile account.", nickname = "getUserAccount", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success ", response = String.class),
            @ApiResponse(code = 409, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint returns all users from database", nickname = "addFavAnnouncement", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - fav announcement added.", response = String.class),
            @ApiResponse(code = 400, message = "Bad request - session not existent(admin not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<List<UserDTO>> getAllUsers (Request request, Response response) {
        List<UserDTO> users = new LinkedList<>();
        users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/deleteAccount", method = RequestMethod.POST)
    @ApiOperation(value = "This endpoint deletes user account.", nickname = "deleteUser", response = String.class)
    @ResponseHeader
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - account deleted.", response = String.class),
            @ApiResponse(code = 400, message = "Bad request - session not existent(user not logged in)."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> deleteUserAccount(Request request, Response response){
        userService.deleteUserAccount(Long.parseLong(request.getParameter("id")));
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }


}
