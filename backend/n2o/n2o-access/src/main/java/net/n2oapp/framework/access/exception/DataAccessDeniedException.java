package net.n2oapp.framework.access.exception;

import org.springframework.http.HttpStatus;

/**
 * Ошибка "Данные недоступны"
 */
public class DataAccessDeniedException extends N2oSecurityException {

    public DataAccessDeniedException(String techMessage) {
        super("n2o.exceptions.forbidden", techMessage);
        setHttpStatus(HttpStatus.FORBIDDEN.value());
    }

}
