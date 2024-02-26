package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarID;
    @Column(name = "date")
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "average_emotion")
    private Emotion averageEmotion;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    protected Calendar() { }

    public Calendar(Long calendarID, LocalDateTime dateTime, Emotion averageEmotion) {
        this.calendarID = calendarID;
        this.dateTime = dateTime;
        this.averageEmotion = averageEmotion;
    }

    public Calendar(Long calendarID, LocalDateTime dateTime, Emotion averageEmotion, Client client) {
        this.calendarID = calendarID;
        this.dateTime = dateTime;
        this.averageEmotion = averageEmotion;
        this.client = client;
    }

    public Long getCalendarID() {
        return calendarID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime date) {
        this.dateTime = date;
    }

    public Emotion getAverageEmotion() {
        return averageEmotion;
    }

    public void setAverageEmotion(Emotion averageEmotion) {
        this.averageEmotion = averageEmotion;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
