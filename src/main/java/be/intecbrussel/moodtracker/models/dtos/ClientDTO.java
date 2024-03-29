package be.intecbrussel.moodtracker.models.dtos;

import be.intecbrussel.moodtracker.validators.ValidEmail;
import be.intecbrussel.moodtracker.validators.ValidPassword;

public class ClientDTO {

    private Long clientID;
    private String userName;
    @ValidEmail
    private String email;
    @ValidPassword
    private String password;

    public ClientDTO() {
    }

    public ClientDTO(Long clientID, String userName, String email, String password) {
        this.clientID = clientID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
