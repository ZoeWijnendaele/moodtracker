package be.intecbrussel.moodtracker.models.dtos;

import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.validators.ValidEmail;
import be.intecbrussel.moodtracker.validators.ValidPassword;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ProfileDTO {

    private Long clientID;
    @NotEmpty(message = "Please provide a username")
    private String userName;
    @NotEmpty(message = "Please provide a email")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Please provide a password")
    @ValidPassword
    private String password;
    @NotNull(message= "Please provide a birthday")
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Avatar avatar;

    public ProfileDTO() {
    }

    public ProfileDTO(Long clientID, String userName, String email, String password, LocalDate birthday, Avatar avatar) {
        this.clientID = clientID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.avatar = avatar;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

}
