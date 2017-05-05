package com.sgbd;

//import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.dto.EstateDTO;
import com.sgbd.dto.PaginatedEstatesDetails;
import com.sgbd.model.Attachement;
import com.sgbd.model.Estate;
import com.sgbd.repository.EstateRepository;
import com.sgbd.util.AttachType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static com.sgbd.model.Estate.ESTATE_ID_COLUMN_NAME;
import static com.sgbd.util.AppConstants.UPLOAD_PATH;

/**
 * Created by mihae on 4/8/2017.
 */
@Service
@Transactional
public class EstateServiceImpl implements EstateService {

    @Autowired
    EstateRepository estateRepository;

    @Override
    public Serializable findById (Long userId) {
        return estateRepository.findByAttribute(ESTATE_ID_COLUMN_NAME, userId, Estate.class);
    }


    @Override
    public PaginatedEstatesDetails getEstatesByFilters(String queryString){
        String[] filter = queryString.split("&");
        Map<String,String> filters = new HashMap<>();
        for (int index = 0; index < filter.length; index ++){
            String []currentParameter = filter[index].split("=");
            filters.put(currentParameter[0],currentParameter[1]);
        }
        String queryFilters = "";

        if(filters.get("city") != null) {
            queryFilters += " upper(city) = '" + filters.get("city").toUpperCase() + "'" ;
        }
        if(filters.get("type") != null) {
            queryFilters += " AND" +  " TYPE_OF_ESTATE = '" + filters.get("type") + "'";
        }
        if(filters.get("square") != null) {
            if (filters.get("square").equals("29") ) {
                queryFilters += " AND" +  " surface < 30 ";
            } else {
                if (filters.get("square").equals(150) ) {
                    queryFilters += " AND" + " surface > 150 ";
                } else {
                    queryFilters += " AND" + " surface between " + filters.get("square")
                            + " AND " + (Integer.parseInt(filters.get("square")) + 20);
                }
            }
        }
        if(filters.get("minPrice") != null) {
            queryFilters += " AND" +  " min_price <= " + filters.get("minPrice");
        }
        if(filters.get("maxPrice") != null) {
            queryFilters += " AND" +  " max_price >= " + filters.get("minPrice");
        }
        if(filters.get("year") != null) {
            switch (filters.get("year")){
                case "1977":
                    queryFilters += " AND  year_of_construction between 1977 and 1990";
                    break;
                case "1800":
                    queryFilters += " AND  year_of_construction between 1800 and 1977";
                    break;
                case "1990":
                    queryFilters += " AND  year_of_construction between 1990 and 2000";
                    break;
                case "2000":
                    queryFilters += " AND  year_of_construction between 2000 and 2017";
            }
        }
        if(filters.get("transType") != null) {
            queryFilters += " AND" +  " upper(TYPE_OF_TRANSACTION) = '" + filters.get("transType").toUpperCase() + "' ";
        }


        Integer offset = 0;
        if (filters.get("offset") != null) {
            offset = Integer.parseInt(filters.get("offset"));
        }
        return estateRepository.getEstatesByFilters(queryFilters,offset);
    }

    @Override
    @Transactional
    public Serializable saveEstate(EstateDTO estateDTO) {
        Estate estate = createEstate(estateDTO);
        if (estateDTO.getBuyPrice() != 0 ){
            estate.setTypeOfTransaction("RENT");
        } else {
            estate.setTypeOfTransaction("SALE");
        }
        estate = (Estate) estateRepository.save(estate, Estate.class);
        Set<Attachement> announcementAttachements = new HashSet<>();
        String[] announcementAttachementsImagesNames = estateDTO.getAnnouncementImagesArray().toArray(new String[estateDTO.getAnnouncementImagesArray().size()]);
        String[] announcementAttachementsImagesIconURI = estateDTO.getAnnouncementImagesIconsURIArray().toArray(new String[estateDTO.getAnnouncementImagesIconsURIArray().size()]);
        for(int index = 0; index < announcementAttachementsImagesNames.length; index ++){
            Attachement attachement = new Attachement(UPLOAD_PATH + File.separator + announcementAttachementsImagesNames[index], AttachType.JPEG);
            attachement.setIconUri(announcementAttachementsImagesIconURI[index]);
            attachement.setIdAnnouncement(estate.getID());
            announcementAttachements.add(attachement);
        }
        estate.setEstateAttachements(announcementAttachements);

        return estateRepository.saveOrUpdate(estate);
    }

    private Estate createEstate(EstateDTO estateDTO) {
        Long price = -1l;
        String estateTransactionType = "";
        if (estateDTO.getBuyPrice() != 0) {
            price = estateDTO.getBuyPrice();
            estateTransactionType = "sale";
        } else {
            estateTransactionType = "rent";
        }
        Date date = new Date();
        Estate estate = new Estate(estateDTO.getRealEstateType(),estateDTO.getAddressLat() + " " + estateDTO.getAddressLng(),
                estateDTO.getSurface(),estateDTO.getRoomsNumber(),estateDTO.getRentPrice(),estateDTO.getBuyPrice(),
                estateDTO.getDivision(),estateDTO.getConstructionYear(),estateDTO.getDescription(),
                date,date,estateDTO.getCity(),estateDTO.getContactNumber(), 1,estateDTO.getUtilities(), estateTransactionType,
                estateDTO.getLevelOfComfort(), estateDTO.getBathrooms(), estateDTO.getCarDisposal(), estateDTO.getFloor());
       return estate;
    }

}
