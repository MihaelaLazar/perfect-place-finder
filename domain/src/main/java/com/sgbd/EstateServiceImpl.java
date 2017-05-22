package com.sgbd;
import com.sgbd.dto.*;
import com.sgbd.model.Attachement;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;
import com.sgbd.repository.AttachementRepository;
import com.sgbd.repository.EstateRepository;
import com.sgbd.repository.UserRepository;
import com.sgbd.util.AttachType;
import com.sgbd.util.CityConstants;
import com.sgbd.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    AttachementRepository attachementRepository;

    @Override
    public Serializable findById (Long userId) {
        return estateRepository.findByAttribute(ESTATE_ID_COLUMN_NAME, userId, Estate.class);
    }

    @Override
    public List<Estate> getUserEstates(Long id){
        return estateRepository.getUserEstates(id);
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
    public Serializable saveEstate(EstateDTO estateDTO, Long idUser) throws InvalidPropertiesFormatException {

        Estate estate;
        // persist announcement
        estate = createEstate(estateDTO, idUser);
        estateDTO.setCity(estateDTO.getCity().split(" ")[0]);
        if (!validateCity(estateDTO.getCity())) {
            throw new InvalidPropertiesFormatException("Invalid city");
        }
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
            attachement.setImageName(announcementAttachementsImagesNames[index]);
            attachement.setIdAnnouncement(estate.getID());
            announcementAttachements.add(attachement);
        }
        estate.setEstateAttachements(announcementAttachements);

        return estateRepository.saveOrUpdate(estate);
    }

    private boolean validateCity(String city) {
        boolean isValidCity = false;
        CityConstants[] constants = CityConstants.values();
        for (CityConstants cityConstant: constants) {
            if (city.equalsIgnoreCase(cityConstant.getAttachType())){
                isValidCity = true;
            }
        }
        return isValidCity;
    }

    @Override
    @Transactional
    public Serializable updateEstate(EstateUpdateDTO estateUpdateDTO) throws IOException {
        Estate estate = (Estate) estateRepository.findByAttribute("id", estateUpdateDTO.getIdEstate(), Estate.class );
        estate.setBathrooms(estateUpdateDTO.getBathrooms());
        estate.setBuyPrice(estateUpdateDTO.getBuyPrice());
        estate.setRentPrice(estateUpdateDTO.getRentPrice());
        estate.setCarDisposal(estateUpdateDTO.getCarDisposal());
        estate.setLastUpdate(new Date());
        estate.setDescription(estateUpdateDTO.getDescription());
        estate.setFloor(estateUpdateDTO.getFloor());
        estate.setLevelOfComfort(estateUpdateDTO.getLevelOfComfort());
        estate.setSurface(estateUpdateDTO.getSurface());
        estate.setRooms(estateUpdateDTO.getRoomsNumber());
        estate.setUtilities(estateUpdateDTO.getUtilities());
        String estateTransactionType = "";
        if (estateUpdateDTO.getBuyPrice() != 0) {
            estateTransactionType = "sale";
        } else {
            estateTransactionType = "rent";
        }
        estate.setTypeOfTransaction(estateTransactionType);
        Set<Attachement> announcementAttachements = new HashSet<>();
        String[] announcementAttachementsImagesNames = estateUpdateDTO.getAnnouncementImagesArray().toArray(new String[estateUpdateDTO.getAnnouncementImagesArray().size()]);

        Iterator<Attachement> it = estate.getEstateAttachements().iterator();
        while(it.hasNext()){
            it.remove(); //<--- iterator safe remove
        }

        estate = estateRepository.saveOrUpdate(estate);
        estate = (Estate)estateRepository.findByAttribute("id", estate.getID(), Estate.class);

        for(int index = 0; index < announcementAttachementsImagesNames.length; index ++) {
            Attachement attachement = new Attachement( announcementAttachementsImagesNames[index], AttachType.JPEG);
            attachement.setIconUri(ImageUtil.convertToURI(announcementAttachementsImagesNames[index]));
            char regex = '\\';
            attachement.setImageName(announcementAttachementsImagesNames[index].split("\\\\")[2]);
            attachement.setIdAnnouncement(estate.getID());
            announcementAttachements.add(attachement);
        }
        estate.getEstateAttachements().addAll(announcementAttachements);
        return estateRepository.saveOrUpdate(estate);
    }

    @Override
    public void deleteEstate(Long estateId) {
        Estate estate = (Estate) estateRepository.findByAttribute("id", estateId, Estate.class);
        estateRepository.deleteAnnouncement(estate, Estate.class);
    }

    private Estate createEstate(EstateDTO estateDTO, Long idUser) {
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
                date,date,estateDTO.getCity(),estateDTO.getContactNumber(), idUser,estateDTO.getUtilities(), estateTransactionType,
                estateDTO.getLevelOfComfort(), estateDTO.getBathrooms(), estateDTO.getCarDisposal(), estateDTO.getFloor());
        return estate;
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        Estate estate = (Estate) estateRepository.findByAttribute("id", messageDTO.getEstateId(), Estate.class);
        if (estate != null) {
            String text = "Hello,I'm "+ messageDTO.getName() +", I found this announcement on your site. Please, give me more details about ID " + messageDTO.getEstateId()
                    + ".  I will be available to achieve/move in from " + messageDTO.getDateToMove() + "." ;
            String secondPartText = "Call me on: " + messageDTO.getPhone() + " or email me on: " + messageDTO.getEmail() + ".";
            Message message = new Message(messageDTO.getEstateId(), text,secondPartText, new Date());
            message.setIdAnnouncement(estate.getID());
            message.setCreatedAtToString();
            estate.getEstateMessages().add(message);
            estateRepository.saveOrUpdate(estate);
        }
    }

    @Override
    @Transactional
    public void deleteMessage(MessageToDeleteDTO messageToDeleteDTO) {
        Estate estate = (Estate) estateRepository.findByAttribute("id", messageToDeleteDTO.getIdAnnouncement(), Estate.class);
        List<Message> messages = new ArrayList<>();
        for(Message message : estate.getEstateMessages()) {
            Message messageToAdd = new Message(message.getIdAnnouncement(), message.getText(),
                    message.getSecondPartText(), message.getCreatedAt());
            messageToAdd.setId(message.getId());
            messages.add(messageToAdd);
        }
        Set<Message> messagesToKeep = new HashSet<>();
        for (int index = 0; index < messages.size(); index ++){
            if (!Objects.equals(messages.get(index).getId(), messageToDeleteDTO.getIdMessage())){
//                messagesToKeep.add(messages.get(index));
                messagesToKeep.add(new Message(messages.get(index).getIdAnnouncement(), messages.get(index).getText(),
                        messages.get(index).getSecondPartText(), messages.get(index).getCreatedAt()));
            }
        }
        Iterator<Message> it = estate.getEstateMessages().iterator();
//        while(it.hasNext()){
//            it.remove(); //<--- iterator safe remove
//        }
        estate.getEstateMessages().clear();

        estate = estateRepository.saveOrUpdate(estate);
        estate = (Estate)estateRepository.findByAttribute("id", estate.getID(), Estate.class);
        estate.getEstateMessages().addAll(messagesToKeep);
//        estate.setEstateMessages(messagesToKeep);
        estateRepository.saveOrUpdate(estate);
    }

    @Override
    public List<Estate> getAllEstates() {
        return estateRepository.getAllEstates();
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = estateRepository.getAllMessages();
        return messages;
    }
}
