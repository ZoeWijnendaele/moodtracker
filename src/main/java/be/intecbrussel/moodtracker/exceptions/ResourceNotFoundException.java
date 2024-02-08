package be.intecbrussel.moodtracker.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super("%s with %s: '%s' not found in database".formatted(resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String resourceName, Long fieldName, Long fieldValue) {
        super("%s with %s: '%s' not found in database".formatted(resourceName, fieldName, fieldValue));
    }

    public HttpStatus getStatus() {
        return status;
    }

}
