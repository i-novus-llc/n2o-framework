package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;

/**
 *
 */
public class N2oConfigConflictException extends N2oException {
    public N2oConfigConflictException() {
    }

    public N2oConfigConflictException(Throwable cause) {
        super(cause);
    }

    public N2oConfigConflictException(String message) {
        super(message);
    }

    public N2oConfigConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
