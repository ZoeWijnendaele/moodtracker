package be.intecbrussel.moodtracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Mood {

    @Id
    private Long moodID;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
