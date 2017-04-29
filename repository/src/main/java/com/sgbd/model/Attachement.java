package com.sgbd.model;

import com.sgbd.util.AttachType;

import javax.persistence.*;

/**
 * Created by Lazarm on 4/21/2017.
 */

@Entity
@Table(name = "PF_ATTACHEMENTS")
public class Attachement {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ID_ANNOUNCEMENT", nullable = false)
    private Long idAnnouncement;

    @Column(name = "PATH_TO_FILE", nullable = false)
    private String pathToFile;

    @Column(name = "TYPE_OF_ATTACHEMENT", nullable = false)
    private AttachType attachType;

    public Attachement() {
    }

    public Attachement(Long idAnnouncement, String pathToFile, AttachType attachType) {
        this.idAnnouncement = idAnnouncement;
        this.pathToFile = pathToFile;
        this.attachType = attachType;
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

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public AttachType getAttachType() {
        return attachType;
    }

    public void setAttachType(AttachType attachType) {
        this.attachType = attachType;
    }
}
