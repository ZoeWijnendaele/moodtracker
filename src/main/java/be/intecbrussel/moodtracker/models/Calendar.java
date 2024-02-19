package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarID;
    @Column(name = "date")
    private LocalDateTime dateTime;
    @ElementCollection
    @CollectionTable(name = "calendar_emotions")
    @MapKeyColumn(name = "date")
    @Column(name = "emotion")
    @Enumerated(EnumType.STRING)
    private Map<LocalDate, Emotion> averageEmotions = new HashMap<>();

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    protected Calendar() { }

    public Calendar(LocalDateTime date, Client client) {
        this.dateTime = date;
        this.client = client;
    }

    public Long getCalendarID() {
        return calendarID;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime date) {
        this.dateTime = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
