package com.sgbd.model;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "ID_ANNOUNCEMENT", nullable = false)
    private Long idAnnouncement;

    @Column(name = "TEXT", nullable = false)
    private String text;

    @Column(name ="SECOND_PART_TEXT")
    private String secondPartText;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Transient
    private String createdAtToString;

    public String getCreatedAtToString() {
        return createdAtToString;
    }

    public void setCreatedAtToString() {
        this.createdAtToString = this.createdAt.toString().split(" ")[0];
    }

    public Message() {
    }

    public Message( Long idAnnouncement, String text,String secondPartText, Date createdAt) {
        this.idAnnouncement = idAnnouncement;
        this.text = text;
        this.secondPartText = secondPartText;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSecondPartText() {
        return secondPartText;
    }

    public void setSecondPartText(String secondPartText) {
        this.secondPartText = secondPartText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
