package com.sgbd.controller;

import com.sgbd.DTO.EstateDTO;
import com.sgbd.OracleCon;
import com.sgbd.DTO.CitiesDTO;
import com.sgbd.model.Estate;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihaela Lazar on 4/8/2017.
 */
@RestController
public class EstateController {

    public static final int NUMBER_OF_COLUMNS = 13;

    @RequestMapping(path = "/add/property", method = RequestMethod.GET)
    public  ResponseEntity<String>  redirectToAddProperty(Response response,Request request) {
        response.setContentType("text/html");
        try {
            response.sendRedirect("/addPropertyRaluca.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Redirect to add property", HttpStatus.OK);
    }

    @RequestMapping(path = "/add/property", headers="Accept=application/json",method = RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<String>  getAddProperty(Response response,Request request,@RequestBody EstateDTO estateDTO) {
        System.out.println("ADD PROPERTY");
        System.out.println(estateDTO);
//        String postDataCopy = "";
//        for (int index = 1; index < postData.length() -1; index++) {
//            postDataCopy += postData.charAt(index);
//        }
//        String [] fields = postDataCopy.split(",");
//
        try {
            OracleCon.getOracleCon().addProperty(estateDTO);
        } catch (SQLException e) {
            return new ResponseEntity<>("DUPLICATE PROPERTY",HttpStatus.FORBIDDEN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Added property", HttpStatus.OK);
    }

    @RequestMapping(path = "/paginate", method = RequestMethod.GET)
    public Object getPaginatedTable(Request request, Response response) {
        System.out.println("Redirect in paginateTable");
        response.setContentType("text/html");

        try {
            response.sendRedirect("/paginateTable.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(path = "/api/paginate", method = RequestMethod.GET)
    public ResponseEntity<CitiesDTO> getPaginatedTableData(Request request, Response response) {
        System.out.println("Redirect in paginateTable POST");
        response.setContentType("application/json");
        CitiesDTO estatesByCity = new CitiesDTO();
        String Uri = request.getQueryString();
        System.out.println(Uri);
        System.out.println(request.getParameter("start"));
        System.out.println(request.getParameter("columns[0][search][value]"));
        System.out.println(request.getParameter("columns[1][search][value]"));
        String[] filters = new String[14];
        for(int index = 0; index < NUMBER_OF_COLUMNS; index ++) {
            filters[index] = request.getParameter("columns["+ index+"][search][value]");
        }
        try {
            estatesByCity = OracleCon.getOracleCon().getEstates(Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("draw")),filters);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(estatesByCity, HttpStatus.OK);
    }

    @RequestMapping(path = "/paginate/filters", method = RequestMethod.POST)
    public ResponseEntity<List<Estate>> getPaginatedTableDataFilters(Request request, Response response, @RequestBody String postData) {
        System.out.println("Redirect in paginateTable POST");
        response.setContentType("application/json");
        List<Estate> estates = new ArrayList<>(1000);
        List<String> estatesCity = new ArrayList<>();
        String postDataCopy = "";
        for (int index = 1; index < postData.length() -1; index++) {
            postDataCopy += postData.charAt(index);
        }
        String [] filters = postDataCopy.split(",");
        return new ResponseEntity<>(estates,HttpStatus.OK);
    }



}
