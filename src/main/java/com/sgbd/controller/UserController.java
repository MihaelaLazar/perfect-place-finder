package com.sgbd.controller;

import com.sgbd.UserService;
import com.sgbd.dto.LoginDTO;
import com.sgbd.dto.SignUpDTO;
import com.sgbd.model.Estate;
import com.sgbd.model.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;

import static com.sgbd.util.ContentType.JSON;

@RestController
//@RequestMapping(path="/user")
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


    @RequestMapping(path = "/user/create", method = RequestMethod.POST)
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

    @RequestMapping(path = "/user/login", method = RequestMethod.POST)
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
//        request.getSession(true).setAttribute("token",);
//        Cookie cookie = new Cookie("sessionID", );
//        cookie.setSecure(true);
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

//    @RequestMapping(path = "/user/getAnnouncements", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Set<Estate>> getAnnouncements(Response response, Request request) {
//        Long id = Long.parseLong(request.getParameter("id"));
//        Set<Estate> userAnnouncements = null;
//        try {
//            User user = (User) userService.findById(id);
//            return new ResponseEntity<>(user.getAnnouncements(), HttpStatus.OK);
//
//        }catch (EntityNotFoundException e){
//            return new ResponseEntity<>(userAnnouncements, HttpStatus.BAD_REQUEST);
//        }
//    }

//    @RequestMapping(path = "/user/add/favoriteAnnouncement", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<User> addFavAnnouncement(Response response, Request request) {
//        Long id = Long.parseLong(request.getParameter("id"));
//        User user = null;
//        try {
//            user = (User) userService.findById(id);
//            userService.setFavoriteAnnouncement(user,Long.parseLong(request.getParameter("idAnnouncement")));
//            return new ResponseEntity<>(user, HttpStatus.OK);
//
//        }catch (EntityNotFoundException e){
//            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
//        }
//    }

    /**
     * RARES:
     *
     */
//    @RequestMapping(path = "/user/update/profile", method = RequestMethod.POST)
//    public ResponseEntity<String>  updateProfile (Request request, Response response, @RequestBody UpdateDTO updateDTO ) {
//        userService.updateUser(updateDTO);
//    }

    /**
     *  RALUCA:
     */

//    @RequestMapping(path = "/user/get/announcements", method = RequestMethod.GET) // pui din JS in url parametrul : id= <id-ul userului>
//    public ResponseEntity<Set<Estate>> getUserEstates (Request request, Response response) {
//        Set<Estate> estates = userService.getUserEstates(Long.parseLong(request.getParameter("id")));
//        return new ResponseEntity<Set<Estate>>(estates, HttpStatus.OK);
//        //in caz de exceptie, prinzi exceptia si arunci un cod de stare :
////         return new ResponseEntity<Set<Estate>>(null, HttpStatus.BAD_REQUEST);
//
//    }

}
