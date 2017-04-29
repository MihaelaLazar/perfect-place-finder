package com.sgbd.model;

import javax.persistence.*;

/**
 * Created by Lazarm on 4/21/2017.
 */

@Entity
@Table(name = "PF_MESSAGES")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ID_USER", nullable = false)
    private Long idUser;

    @Column(name = "ID_ANNOUNCEMENT", nullable = false)
    private Long idAnnouncement;

    @Column(name = "TEXT", nullable = false)
    private String text;

    public Message() {
    }

    public Message(Long idUser, Long idAnnouncement, String text) {
        this.idUser = idUser;
        this.idAnnouncement = idAnnouncement;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(Long idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
