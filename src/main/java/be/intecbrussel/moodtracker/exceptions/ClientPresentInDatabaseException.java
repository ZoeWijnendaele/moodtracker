package be.intecbrussel.moodtracker.exceptions;

public class ClientPresentInDatabaseException extends RuntimeException {

    public ClientPresentInDatabaseException(String resource, String field, String value) {
    super(String.format("%s with %s: %s already exists", resource, field, value));
    }

}
