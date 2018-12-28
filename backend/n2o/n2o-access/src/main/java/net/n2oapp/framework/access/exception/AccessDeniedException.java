package net.n2oapp.framework.access.exception;

import org.springframework.http.HttpStatus;

/**
 * Ошибка "Нет доступа"
 */
public class AccessDeniedException extends N2oSecurityException {

    public AccessDeniedException() {
        super("n2o.exceptions.forbidden", "Access is denied");
        setHttpStatus(HttpStatus.FORBIDDEN.value());
    }

    public AccessDeniedException(String message) {
        super("n2o.exceptions.forbidden", message);
        setHttpStatus(HttpStatus.FORBIDDEN.value());
    }

}
