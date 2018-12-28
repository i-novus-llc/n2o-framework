package net.n2oapp.framework.api.exception;

/**
 * Не найден обязательный конеткс тпользователя
 */
public class NotFoundContextPlaceholderException extends N2oException {

    public NotFoundContextPlaceholderException(String placeHolder) {
        super("n2o.fieldNotFoundInContext");
        setData(placeHolder);
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }
}
