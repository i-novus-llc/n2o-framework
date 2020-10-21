package net.n2oapp.framework.api.exception;

/**
 * Не найден обязательный конеткс тпользователя
 */
public class NotFoundContextPlaceholderException extends N2oUserException {

    public NotFoundContextPlaceholderException(String placeHolder) {
        super("n2o.fieldNotFoundInContext", "n2o.fieldNotFoundInContext");
        setData(placeHolder);
    }

}
