package be.intecbrussel.moodtracker.repositories;

import be.intecbrussel.moodtracker.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {

}
