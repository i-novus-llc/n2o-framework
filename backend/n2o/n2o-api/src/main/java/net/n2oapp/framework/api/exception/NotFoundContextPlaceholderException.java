package net.n2oapp.framework.api.exception;

/**
 * Исключение, бросаемое в случае отсутствия обязательного контекста пользователя
 */
public class NotFoundContextPlaceholderException extends N2oUserException {

    public NotFoundContextPlaceholderException(String placeholder) {
        super("n2o.fieldNotFoundInContext", "n2o.fieldNotFoundInContext");
        setData(placeholder);
    }
}
