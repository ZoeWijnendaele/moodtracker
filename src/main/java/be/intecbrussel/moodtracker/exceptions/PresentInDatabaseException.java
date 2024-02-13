package be.intecbrussel.moodtracker.exceptions;

public class PresentInDatabaseException extends RuntimeException {

    public PresentInDatabaseException(String resource, String field, String value) {
    super(String.format("%s with %s: %s already exists", resource, field, value));
    }

}
