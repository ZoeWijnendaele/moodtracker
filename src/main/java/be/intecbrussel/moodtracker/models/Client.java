package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Avatar;
import be.intecbrussel.moodtracker.models.enums.Role;
import be.intecbrussel.moodtracker.validators.ValidEmail;
import be.intecbrussel.moodtracker.validators.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientID;
    @NotEmpty(message = "Please provide a username")
    @Column(name = "username")
    private String userName;
    @ValidEmail
    @NotEmpty(message = "Please provide a email")
    @Column(name = "email")
    private String email;
    @ValidPassword
    @NotEmpty(message = "Please provide a password")
    @Column(name = "password")
    private String password;
    @NotNull(message= "Please provide a birthday")
    @Column(name = "birthday")
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    @Column(name = "avatar")
    private Avatar avatar;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "client")
    private Calendar calendar;
    @OneToMany(mappedBy = "client")
    private List<Mood> moods;

    protected Client() { }

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Client(Long clientID, String userName, String email, String password) {
        this.clientID = clientID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Client(Long clientID, String userName, String email, String password, LocalDate birthday, Avatar avatar) {
        this.clientID = clientID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.avatar = avatar;
    }

    public Client(Long clientID, String userName, String email, String password, LocalDate birthday, Avatar avatar, Set<Role> roles, Calendar calendar, List<Mood> moods) {
        this.clientID = clientID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.avatar = avatar;
        this.roles = roles;
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

    public Set<Role> getRole() {
        return roles;
    }

    public void setRole(List<String> role) {
        this.roles = role.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
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
