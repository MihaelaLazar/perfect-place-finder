package com.sgbd;

import com.sgbd.dto.*;
import com.sgbd.model.Estate;
import com.sgbd.model.Message;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public interface EstateService {
    // PaginatedEstatesDetails getEstatesByFilters(String queryString) throws SQLException, ClassNotFoundException;

    Serializable findById(Long userId);

    List<Estate> getUserEstates(Long id);

    PaginatedEstatesDetails getEstatesByFilters(String queryString);

    Serializable saveEstate(EstateDTO estateDTO, Long idUser) throws InvalidPropertiesFormatException, SQLException;

    Serializable updateEstate(EstateUpdateDTO estateUpdateDTO) throws Exception;

    void deleteEstate(Long estateId);

    void sendMessage(MessageDTO messageDTO) throws SQLException;

    void deleteMessage(MessageToDeleteDTO messageToDeleteDTO) throws SQLException;

    List<Estate> getAllEstates();

    List<Message> getAllMessages();
}
