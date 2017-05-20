package com.sgbd;

        import com.sgbd.dto.*;
        import com.sgbd.model.Estate;
        import com.sgbd.model.Message;

        import java.io.IOException;
        import java.io.Serializable;
        import java.util.InvalidPropertiesFormatException;
        import java.util.List;

/**
 * Created by mihae on 4/8/2017.
 */
public interface EstateService {
    // PaginatedEstatesDetails getEstatesByFilters(String queryString) throws SQLException, ClassNotFoundException;

    Serializable findById (Long userId);

    List<Estate> getUserEstates(Long id);

    PaginatedEstatesDetails getEstatesByFilters(String queryString);

    Serializable saveEstate(EstateDTO estateDTO, Long idUser) throws InvalidPropertiesFormatException;

    Serializable updateEstate(EstateUpdateDTO estateUpdateDTO) throws IOException;

    void deleteEstate(Long estateId);

    void sendMessage(MessageDTO messageDTO);

    void deleteMessage(MessageToDeleteDTO messageToDeleteDTO);

    List<Estate> getAllEstates();

    List<Message> getAllMessages();
}
