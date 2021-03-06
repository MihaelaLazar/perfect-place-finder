package com.sgbd.controller;

import com.sgbd.EstateService;
import com.sgbd.OracleCon;
import com.sgbd.UserService;
import com.sgbd.dto.*;
import com.sgbd.model.Attachement;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.util.ImageUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sgbd.util.AppConstants.UPLOAD_PATH;
import static com.sgbd.util.ContentType.JSON;

@RestController
@RequestMapping(path="/estate")
public class EstateController {

    @Autowired
    private EstateService estateService;

    @Autowired
    private UserService userService;

    private static final int NUMBER_OF_COLUMNS = 13;

    @ApiOperation(value = "This endpoint redirects to addProperty.html page.", nickname = "doStuff", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "This endpoint adds a property/announcement.", nickname = "doStuff", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - added property", response = String.class),
            @ApiResponse(code = 409, message = "Conflict - property with same address already existent."),
            @ApiResponse(code = 404, message = "Not Found")}
    )
    @ResponseBody
    public ResponseEntity<String> getAddProperty(Response response, Request request, @RequestBody EstateDTO estateDTO) {
        Long idUser = (Long)request.getSession(false).getAttribute("ID");
        if(estateDTO.getRealEstateType().equals("Commercial space")){
            estateDTO.setRealEstateType("space");
        }
        if(estateDTO.getRealEstateType().equals("Apartment")){
            estateDTO.setRealEstateType("appartment");
        }
        try{
            estateService.saveEstate(estateDTO, idUser);
            return new ResponseEntity<>("Added property", HttpStatus.OK);
        } catch(PersistenceException e) {
            return new ResponseEntity<>("Could not add property", HttpStatus.CONFLICT);
        } catch (InvalidPropertiesFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Could not add property", HttpStatus.CONFLICT);
        } catch (SQLException e) {
            return new ResponseEntity<>("Could not add property", HttpStatus.CONFLICT);
        }
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
        response.setContentType(JSON.getContentType());

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
        response.setContentType(JSON.getContentType());
        List<Estate> estates = new ArrayList<>(1000);
        return new ResponseEntity<>(estates, HttpStatus.OK);
    }

    @RequestMapping(path = "/getByFilters", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint returns a list with estates filtered by given filters.", nickname = "doStuff", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedEstatesDetails.class)}
    )
    public ResponseEntity<PaginatedEstatesDetails> getEstates(Request request, Response response){
        response.setContentType(JSON.getContentType());
        PaginatedEstatesDetails paginatedEstatesDetails = estateService.getEstatesByFilters(request.getQueryString());
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long idUser = (Long) session.getAttribute("ID");
            List<Estate> userFavEstates = userService.getUserFavoriteAnnouncements(idUser);
            List<Long> userFavEstatesIds = new LinkedList<>();
            if (userFavEstates != null && userFavEstates.size() > 0 ) {
                for (int index = 0; index < paginatedEstatesDetails.getEstates().size(); index ++ ) {
                    for (Estate favEstate : userFavEstates) {
                        if (favEstate.getID().equals(paginatedEstatesDetails.getEstates().get(index).getID())){
                            userFavEstatesIds.add(favEstate.getID());
                        }
                    }
                }
            }
            paginatedEstatesDetails.setFavEstates(userFavEstatesIds);
        } else {
        }

        return new ResponseEntity<>(paginatedEstatesDetails, HttpStatus.OK);
    }

    @RequestMapping(path = "/getDetails", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint returns a json response with details about given announcement(estate).", nickname = "doStuff", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - added property", response = Estate.class),
            @ApiResponse(code = 404, message = "Not Found")}
    )
    public ResponseEntity<Estate> getEstateDetails(Request request, Response response) {
        Estate estate = (Estate) estateService.findById(Long.parseLong(request.getParameter("id")));
        response.setContentType(JSON.getContentType());
        Set<Attachement> estateAttachements = new HashSet<>();
        for(Attachement imageName : estate.getEstateAttachements()){
            try {
                String imageURI = ImageUtil.convertToURI(imageName.getPathToFile());
                imageName.setIconUri(imageURI);
                estateAttachements.add(imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        estate.setEstateAttachements(estateAttachements);
        return new ResponseEntity<>(estate, HttpStatus.OK);
    }

    @RequestMapping(path = "/save/image", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ApiOperation(value = "This endpoint saves and announcement attached image.", nickname = "doStuff", response = ImageDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ImageDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ImageDTO saveImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("image") MultipartFile multiPart){
        InputStream inputStream = null;
        String newFile = request.getParameter("image");
        Part filePart = null;
        try {
            filePart = request.getPart("image");
            String fileName = ImageUtil.getFileName(filePart);
            OutputStream out = null;
            InputStream filecontent = null;
            try {
                Timestamp timestamp = new Timestamp(new Date().getTime());
                String fileNameDateStamp = new Date().toString().replace(" ","").replace(":","") + ".jpg";
                out = new FileOutputStream(new File(UPLOAD_PATH + File.separator
                        + fileNameDateStamp/*fileName*/));
                filecontent = filePart.getInputStream();
                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                String originalImagePathname = UPLOAD_PATH + File.separator + fileNameDateStamp;
                String iconPathname = ImageUtil.generateIcon(originalImagePathname, fileNameDateStamp);
                String imageURI = ImageUtil.convertToURI(iconPathname);
                ImageDTO imageDTO = new ImageDTO(iconPathname,fileNameDateStamp,imageURI);
                return imageDTO;
            } catch (FileNotFoundException fne) {
            } finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        if (filePart != null) {
            try {
                inputStream = filePart.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ImageDTO();
    }

    @RequestMapping(path = "/update/property",headers = "Accept=application/json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "This endpoint updates a given announcement of the user logged in.", nickname = "doStuff", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 403, message = "Forbidden - Update property could not execute."),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> updateProperty(Request request, Response response, @RequestBody EstateUpdateDTO estateUpdateDTO) {
        try {
            estateService.updateEstate(estateUpdateDTO);
            return new ResponseEntity<>("Updated property", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Update property could not execute.", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/delete/property", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "This endpoint deletes an announcement", nickname = "deleteProperty", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 403, message = "Forbidden - Could not delete property"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> deleteProperty(Request request, Response response, @RequestBody Long estateId) {
        try{
            estateService.deleteEstate(estateId);
            return new ResponseEntity<>("Deleted property", HttpStatus.OK);
        } catch(PersistenceException e) {
            return new ResponseEntity<>("Could not delete property", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/send/message", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "This endpoint sends a message to the user who posted the announcement.", nickname = "sendMessage", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 403, message = "Forbidden - Could not send message"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Incorrect input fields"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> sendMessage (Request request, Response response, @RequestBody MessageDTO messageDTO) {
        String errorMessage = "";
        if (messageDTO.getPhone() == "") {
            errorMessage += "phone_null ";
        } else {
            Pattern pattern = Pattern.compile("^[0-9]{10}$");
            Matcher matcher = pattern.matcher(messageDTO.getPhone());
            if (! matcher.find()){
                errorMessage += "phone_invalid ";
            }
        }
        if (messageDTO.getEmail() == "" ) {
            errorMessage += "email_null ";
        } else {
            Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(messageDTO.getEmail());
            if (!matcher.find()) {
                errorMessage += "email_invalid ";
            }
        }
        if (messageDTO.getName() == "") {
            errorMessage += "name_null ";
        }
        if (messageDTO.getEstateId() == null ) {
            errorMessage += "idEstate_null ";
        }
        if (messageDTO.getDateToMove() == "") {
            errorMessage += "moveDate_null";
        }
        if (errorMessage == "" ) {
            try {
                estateService.sendMessage(messageDTO);
            } catch (SQLException e) {
                return new ResponseEntity<>("Could not update property", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>("Ok, message sent", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/delete/message", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "This endpoint deletes a message.", nickname = "deleteMessage", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - Message deleted", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 403, message = "Forbidden - Could not send message"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Incorrect input fields"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> deleteMessage (Response response, Request request, @RequestBody MessageToDeleteDTO messageToDeleteDTO) {
        try {
            estateService.deleteMessage(messageToDeleteDTO);
        } catch (SQLException e) {
            return new ResponseEntity<>("Could not delete message", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>("Message deleted", HttpStatus.OK);
    }

    @RequestMapping(path = "/getAllAnnouncements", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint returns all announcements from database (admin page).", nickname = "getAllAnnouncements", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<List<Estate>> getAllAnnouncements(Request request, Response response){
        List<Estate> estates = estateService.getAllEstates();
        return new ResponseEntity<>(estates, HttpStatus.OK);
    }


    @RequestMapping(path = "/getAllMessages", method = RequestMethod.GET)
    @ApiOperation(value = "This endpoint returns all messages from database (admin page).", nickname = "getAllMessages", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized - user not logged in"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<List<Message>> getAllMessages(Request request, Response response){
        List<Message> messages = estateService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


}
