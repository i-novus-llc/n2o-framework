package net.n2oapp.framework.access.exception;

import org.springframework.http.HttpStatus;

/**
 * Ошибка "Пользователь не авторизован"
 */
public class UnauthorizedException extends N2oSecurityException {
    public UnauthorizedException() {
        super("n2o.exceptions.unauthorized", "Unauthorized");
        setHttpStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
