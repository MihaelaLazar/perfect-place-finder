package com.sgbd.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ResourceController {

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

    @RequestMapping(path = "/get/noise/tweets", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> getNoiseTweets (Request request, Response response) {
        List<String> noiseTweets = new ArrayList<>();
        try {
            HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.get("http://localhost:9001/get/noise/tweets")
                    .asJson();
            System.out.println(jsonNodeHttpResponse.getBody().toString());
            System.out.println(jsonNodeHttpResponse.getBody().getArray().length());
//            for (String tweet: jsonNodeHttpResponse.getBody().getArray().)
            for(int index = 0; index < jsonNodeHttpResponse.getBody().getArray().length(); index ++) {
                System.out.println(jsonNodeHttpResponse.getBody().getArray().get(index));
                noiseTweets.add((String) jsonNodeHttpResponse.getBody().getArray().get(index));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(noiseTweets, HttpStatus.OK);
    }

}
