package com.sgbd.controller;

import com.sgbd.EstateService;
import com.sgbd.OracleCon;
import com.sgbd.UserService;
import com.sgbd.dto.*;
import com.sgbd.model.Attachement;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.util.ImageUtil;
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

//        try {
//            OracleCon.getOracleCon().addProperty(estateDTO);
//        } catch (SQLException e) {
//            return new ResponseEntity<>("DUPLICATE PROPERTY", HttpStatus.FORBIDDEN);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (DataIntegrityViolationException e) {
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
    public ResponseEntity<String> updateProperty(Request request, Response response, @RequestBody EstateUpdateDTO estateUpdateDTO) {
        try{
            estateService.updateEstate(estateUpdateDTO);
            return new ResponseEntity<>("Updated property", HttpStatus.OK);
        } catch(PersistenceException e) {
            return new ResponseEntity<>("Could not update property", HttpStatus.FORBIDDEN);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not update property", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/delete/property", method = RequestMethod.POST)
    @ResponseBody
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
    public ResponseEntity<String> sendMessage (Request request, Response response, @RequestBody MessageDTO messageDTO) {
        System.out.println(messageDTO.getDateToMove());
        String errorMessage = "";
        if (messageDTO.getPhone() == "") {
            errorMessage += "phone ";
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
            errorMessage += "name ";
        }
        if (messageDTO.getEstateId() == null ) {
            errorMessage += "idEstate ";
        }
        if (messageDTO.getDateToMove() == "") {
            errorMessage += "moveDate_null";
        }
        if (errorMessage == null ) {
            estateService.sendMessage(messageDTO);
            return new ResponseEntity<String>("Ok, message sent", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/delete/message", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteMessage (Response response, Request request, @RequestBody MessageToDeleteDTO messageToDeleteDTO) {
        estateService.deleteMessage(messageToDeleteDTO);
        return new ResponseEntity<String>("Message deleted", HttpStatus.OK);
    }

    @RequestMapping(path = "/getAllAnnouncements", method = RequestMethod.GET)
    public ResponseEntity<List<Estate>> getAllAnnouncements(Request request, Response response){
        List<Estate> estates = estateService.getAllEstates();
        return new ResponseEntity<>(estates, HttpStatus.OK);
    }

    @RequestMapping(path = "/getAllMessages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getAllMessages(Request request, Response response){
        List<Message> messages = estateService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


}
