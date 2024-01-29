package be.intecbrussel.moodtracker.security;

import be.intecbrussel.moodtracker.models.Client;
import be.intecbrussel.moodtracker.repositories.ClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public CustomUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findById(email);

        if (client.isEmpty()) {
            throw new UsernameNotFoundException(email + " not found");
        }

        Client activeUser = client.get();

        return org.springframework.security.core.userdetails.User.builder()
                .username(activeUser.getEmail())
                .password(activeUser.getPassword())
                .roles(activeUser.getRole().name())
                .build();
    }

}
