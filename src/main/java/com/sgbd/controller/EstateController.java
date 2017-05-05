package com.sgbd.controller;

import com.sgbd.EstateService;
import com.sgbd.OracleCon;
import com.sgbd.dto.CitiesDTO;
import com.sgbd.dto.EstateDTO;
import com.sgbd.dto.ImageDTO;
import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.model.Attachement;
import com.sgbd.model.Estate;
import com.sgbd.util.ImageUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static com.sgbd.util.ContentType.JSON;

@RestController
@RequestMapping(path="/estate")
public class EstateController {

    @Autowired
    private EstateService estateService;

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
        if(estateDTO.getRealEstateType().equals("Commercial space")){
            estateDTO.setRealEstateType("space");
        }
        if(estateDTO.getRealEstateType().equals("Apartment")){
            estateDTO.setRealEstateType("appartment");
        }
        try{
            estateService.saveEstate(estateDTO);
            return new ResponseEntity<>("Added property", HttpStatus.OK);
        } catch(PersistenceException e) {
            return new ResponseEntity<>("Could not add property", HttpStatus.FORBIDDEN);
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
                out = new FileOutputStream(new File("C:\\upload" + File.separator
                        + fileNameDateStamp/*fileName*/));
                filecontent = filePart.getInputStream();
                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                String originalImagePathname = "C:\\upload" + File.separator + fileNameDateStamp;
                String iconPathname = ImageUtil.generateIcon(originalImagePathname, fileNameDateStamp);
                String imageURI = ImageUtil.convertToURI(iconPathname);
                ImageDTO imageDTO = new ImageDTO(fileNameDateStamp,imageURI);
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

}
