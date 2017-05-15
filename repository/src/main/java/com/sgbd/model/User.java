package com.sgbd.model;

import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Lazarm
 * Creation Date: 4/3/2017.
 */
@Entity
@Table(name = "PF_USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "EMAIL")
})
public class User implements Serializable {

    public static String USER_EMAIL_COLUMN_NAME = "email";
    public static String USER_ID_COLUMN_NAME = "id";

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name="EMAIL",nullable = false)
    private String email;

    @Column(name="FIRSTNAME", nullable = false)
    private String firstName;

    @Column(name="LASTNAME" , nullable = false)
    private String lastName;

    @Column(name="PASSWORD", nullable = false)
    private String password;

//    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @JoinTable(name="PF_FAV_ANNOUNCEMENTS",
//            joinColumns = { @JoinColumn(name = "ID_USER")},
//            inverseJoinColumns = {@JoinColumn(name="ID_ANNOUNCEMENT")}
//    )
//    private Set<Estate> favoriteAnnouncements;

    @Transient
    private boolean enabled;

    @Transient
    private boolean locked;


    public User() {
    }

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(Long id, String email, String firstName, String lastName,
                String password) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private boolean isLocked() {
        return locked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", firstName="
                + firstName + ", lastName=" + lastName + ", password="
                + password + "]";
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private User user;

        public Builder() {
            user = new User();
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            user.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            user.lastName = lastName;
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

//        public Builder role(Role role) {
//            user.userRoles.add(new UserRole(this.user, role));
//            return this;
//        }

        public Builder enabled(boolean enabled) {
            user.enabled = enabled;
            return this;
        }

        public Builder locked(boolean locked) {
            user.locked = locked;
            return this;
        }


        public User build() {
            return user;
        }

        public Builder id(Long id) {
            user.id = id;
            return this;
        }
    }

    public String getUsername() {
        return getEmail();
    }

    public boolean isAccountNonLocked() {
        return !isLocked();
    }
    public boolean isEnabled() {
        return enabled;
    }
}