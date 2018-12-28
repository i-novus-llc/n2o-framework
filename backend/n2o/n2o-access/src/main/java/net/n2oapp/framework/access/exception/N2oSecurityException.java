package net.n2oapp.framework.access.exception;

import net.n2oapp.framework.api.exception.N2oUserException;

/**
 * Стандратное исключение при проверке доступа
 */
public class N2oSecurityException extends N2oUserException {

    public N2oSecurityException() {
        super("n2o.exceptions.unauthorized");

    }

    public N2oSecurityException(String userMessage, String techMessage) {
        super(userMessage, techMessage);
    }

}
