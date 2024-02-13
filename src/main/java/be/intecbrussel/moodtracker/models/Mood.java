package be.intecbrussel.moodtracker.models;

import be.intecbrussel.moodtracker.models.enums.Emotion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mood_id")
    private Long moodID;
    @NotEmpty(message = "Please provide an emotion")
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;
    @NotEmpty(message = "Please provide a rating")
    @Column(name = "rating")
    private int rating;
    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    protected Mood() { }

    public Mood(Long moodID, Emotion emotion, int rating, String description) {
        this.moodID = moodID;
        this.emotion = emotion;
        this.rating = rating;
        this.description = description;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}
