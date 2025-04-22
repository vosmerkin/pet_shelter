package ua.tc.marketplace.exception.security;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when a user does not have the necessary permissions to perform an action.
 * <p>
 * This exception extends {@link CustomRuntimeException} and is used to indicate
 * that a user's role does not grant sufficient privileges for the attempted operation.
 * </p>
 */
public class UnauthorizedException extends CustomRuntimeException {
    private static final String ERROR_MESSAGE = "User role %s doesn't allow to take the action";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    /**
     * Constructs a new {@code UnauthorizedException} with a formatted error message
     * indicating the user's role that caused the authorization failure.
     *
     * @param role The role of the user attempting the unauthorized action.
     */
    public UnauthorizedException(String role) {
        super(ERROR_MESSAGE.formatted(role), STATUS);
    }
}
