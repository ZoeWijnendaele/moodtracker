package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientID;
    @NotEmpty
    @Column(name = "username")
    private String userName;
    @Email
    @Column(name = "email")
    private String email;
    @NotEmpty(message = "Please provide a password")
    @Column(name = "password")
    private String password;
    @NotEmpty(message= "Please provide a birthday")
    @Column(name = "birthday")
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    @Column(name = "avatar")
    private Avatar avatar;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Calendar calendar;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Mood> moods;

    protected Client() { }

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Client(String userName, String email, String password, LocalDate birthday, Avatar avatar) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.avatar = avatar;
    }

    public Client(Long clientID, String userName, String email, String password, LocalDate birthday, Avatar avatar, Role role, Calendar calendar, List<Mood> moods) {
        this.clientID = clientID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.avatar = avatar;
        this.role = role;
        this.calendar = calendar;
        this.moods = moods;
    }

    public Long getClientID() {
        return clientID;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public List<Mood> getMoods() {
        return moods;
    }

    public void setMoods(List<Mood> moods) {
        this.moods = moods;
    }

}
