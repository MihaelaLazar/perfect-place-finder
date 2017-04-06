package com.sgbd.model;

import java.io.Serializable;

/**
 * Created by mihae on 4/3/2017.
 */

public class User  implements Serializable {

    public static final String USER_EMAIL_COLUMN_NAME = "email";
    public static final String USER_ID_COLUMN_NAME = "id";

    private Long id;


    private String email;

    private String firstName;


    private String lastName;


    private String password;


    private boolean enabled;

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