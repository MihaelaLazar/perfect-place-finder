package com.sgbd.dto;

public class MessageToDeleteDTO {

    private Long idMessage;
    private Long idAnnouncement;

    public MessageToDeleteDTO() {
    }

    public MessageToDeleteDTO(Long idMessage, Long idAnnouncement) {
        this.idMessage = idMessage;
        this.idAnnouncement = idAnnouncement;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public Long getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(Long idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }
}
