package senta.nilesh.autocalc.dto;

import java.io.Serializable;

/**
 * Created by "Nilesh Senta" on 4/7/2016.
 */
public class UserProfileDTO implements Serializable {
    private String userName;
    private String password;
    private String firstName;
    private boolean isGuest;
    private String lastName;
    private String phone;
    private String email;
    private String registerToken;


    public UserProfileDTO() {
    }

    public UserProfileDTO(String userName, String password, String firstName, String lastName, String phone, String email, boolean isGuest, String registerToken) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.isGuest = isGuest;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.registerToken = registerToken;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
