package be.intecbrussel.moodtracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Calendar {

    @Id
    private Long calendarID;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
