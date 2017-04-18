package com.sgbd.controller;

import com.sgbd.OracleCon;
import com.sgbd.dto.CitiesDTO;
import com.sgbd.dto.EstateDTO;
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

@RestController
public class EstateController {

    private static final int NUMBER_OF_COLUMNS = 13;

    @RequestMapping(path = "/add/property", method = RequestMethod.GET)
    public ResponseEntity<String> redirectToAddProperty(Response response, Request request) {
        response.setContentType("text/html");

        try {
            response.sendRedirect("/addProperty.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Redirect to add property", HttpStatus.OK);
    }

    @RequestMapping(path = "/add/property", headers = "Accept=application/json", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getAddProperty(Response response, Request request, @RequestBody EstateDTO estateDTO) {

        try {
            OracleCon.getOracleCon().addProperty(estateDTO);
        } catch (SQLException e) {
            return new ResponseEntity<>("DUPLICATE PROPERTY", HttpStatus.FORBIDDEN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Added property", HttpStatus.OK);
    }

    @RequestMapping(path = "/paginate", method = RequestMethod.GET)
    public Object getPaginatedTable(Request request, Response response) {
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
        response.setContentType("application/json");

        CitiesDTO estatesByCity = new CitiesDTO();
        String[] filters = new String[14];
        for (int index = 0; index < NUMBER_OF_COLUMNS; index++) {
            filters[index] = request.getParameter("columns[" + index + "][search][value]");
        }
        try {
            estatesByCity = OracleCon.getOracleCon().getEstates(Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("draw")), filters);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(estatesByCity, HttpStatus.OK);
    }

    @RequestMapping(path = "/paginate/filters", method = RequestMethod.POST)
    public ResponseEntity<List<Estate>> getPaginatedTableDataFilters(Request request, Response response, @RequestBody String postData) {
        response.setContentType("application/json");

        List<Estate> estates = new ArrayList<>(1000);

        return new ResponseEntity<>(estates, HttpStatus.OK);
    }


}
