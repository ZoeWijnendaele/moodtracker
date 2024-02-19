package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mood_id")
    private Long moodID;
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;
    @Column(name = "rating")
    private int rating;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;

    protected Mood() { }

    public Mood(Long moodID, Emotion emotion, int rating) {
        this.moodID = moodID;
        this.emotion = emotion;
        this.rating = rating;
    }

    public Mood(Long moodID, Emotion emotion, int rating, String description) {
        this.moodID = moodID;
        this.emotion = emotion;
        this.rating = rating;
        this.description = description;
    }

    public Mood(Long moodID, Emotion emotion, int rating, String description, Client client) {
        this.moodID = moodID;
        this.emotion = emotion;
        this.rating = rating;
        this.description = description;
        this.client = client;
    }

    public Long getMoodID() {
        return moodID;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}
