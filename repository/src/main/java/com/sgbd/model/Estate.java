package com.sgbd.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mihae on 4/8/2017.
 */

@SuppressWarnings("ALL")
@Entity
@Table(name = "PF_ANNOUNCEMENTS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ADDRESS")})
public class Estate {

    public static final String ESTATE_ID_COLUMN_NAME = "id";

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long ID;

    @Column(name = "TYPE_OF_ESTATE", nullable = false)
    private String type;

    @Column(name = "TYPE_OF_TRANSACTION", nullable = false)
    private String typeOfTransaction;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "SURFACE", nullable = false)
    private Long surface;

    @Column(name = "ROOMS", nullable = false)
    private Long rooms;

    @Column(name = "RENT_PRICE", nullable = false)
    private Long rentPrice;

    @Column(name = "BUY_PRICE", nullable = false)
    private Long buyPrice;

    @Column(name = "DIVISION", nullable = false)
    private String division;

    @Column(name = "YEAR_OF_CONSTRUCTION", nullable = false)
    private int constructionYear;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "CREATION_DATE", nullable = false)
    @Type(type = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "LAST_UPDATE", nullable = false)
    @Type(type = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @Column(name = "CITY", nullable = false)
    private String city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "ID_ANNOUNCEMENT")
    private Set<Message> estateMessages;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "ID_ANNOUNCEMENT")
    private Set<Attachement> estateAttachements;

    public Set<Attachement> getEstateAttachements() {
        return estateAttachements;
    }

    public Set<Message> getEstateMessages() {
        return this.estateMessages;
    }

    public void setEstateMessages(Set<Message> estateMessages) {
        this.estateMessages = estateMessages;
    }

    public Estate(Long ID, String type, String address, Long surface, Long rooms, Long rentPrice, Long buyPrice, String division, int constructionYear, String description, Date creationDate, Date lastUpdate, String city) {
        this.ID = ID;
        this.type = type;
        this.address = address;
        this.surface = surface;
        this.rooms = rooms;
        this.rentPrice = rentPrice;
        this.buyPrice = buyPrice;
        this.division = division;
        this.constructionYear = constructionYear;
        this.description = description;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
        this.city = city;
    }

    public Estate() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getSurface() {
        return surface;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
    }

    public Long getRooms() {
        return rooms;
    }

    public void setRooms(Long rooms) {
        this.rooms = rooms;
    }

    public Long getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Long rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Long getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Long buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public void setTypeOfTransaction(String typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
    }

    public void setEstateAttachements(Set<Attachement> estateAttachements) {
        this.estateAttachements = estateAttachements;
    }
}
