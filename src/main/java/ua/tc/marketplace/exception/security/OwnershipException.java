package ua.tc.marketplace.exception.security;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when a user attempts to access a resource they do not own.
 * <p>
 * This exception extends {@link CustomRuntimeException} and is used to indicate
 * that a user with the given email does not have ownership rights over the requested resource.
 * </p>
 */
public class OwnershipException extends CustomRuntimeException {
    private static final String ERROR_MESSAGE = "User with email %s doesn't own this resource";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    /**
     * Constructs a new {@code OwnershipException} with a formatted error message
     * indicating the unauthorized user's email.
     *
     * @param email The email of the user who attempted to access the resource.
     */
    public OwnershipException(String email) {
        super(ERROR_MESSAGE.formatted(email), STATUS);
    }
}
