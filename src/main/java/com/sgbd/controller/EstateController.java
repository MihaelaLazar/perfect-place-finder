package com.sgbd.controller;

import com.sgbd.OracleCon;
import com.sgbd.model.CitiesDTO;
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

    @RequestMapping(path = "/addProperty", method = RequestMethod.GET)
    public @ResponseBody
    String getAddProperty(Response response) {
        System.out.println();
        response.setContentType("text/html");

        try {
            response.sendRedirect("/addPropertyRaluca.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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
        OracleCon oracleCon = new OracleCon();
        CitiesDTO estatesByCity = new CitiesDTO();
        String Uri = request.getQueryString();
        System.out.println(Uri);
        System.out.println(request.getParameter("start"));
        System.out.println(request.getParameter("columns[0][search][value]"));
        System.out.println(request.getParameter("columns[1][search][value]"));
        String[] filters = new String[14];
        for(int index = 0; index < 13; index ++) {
            filters[index] = request.getParameter("columns["+ index+"][search][value]");
        }
        try {
            estatesByCity = oracleCon.getEstates(Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("draw")),filters);
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
        OracleCon oracleCon = new OracleCon();
        List<String> estatesCity = new ArrayList<>();
        String postDataCopy = "";
        for (int index = 1; index < postData.length() -1; index++) {
            postDataCopy += postData.charAt(index);
        }
        String [] filters = postDataCopy.split(",");
//        try {
//            estates = oracleCon.getEstates();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        return new ResponseEntity<>(estates,HttpStatus.OK);
    }
}
