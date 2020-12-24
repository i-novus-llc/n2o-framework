package net.n2oapp.framework.api.data.validation;

/**
 * Callback ошибки валидации
 */
public interface ValidationFailureCallback {
    void onFail(String msg);
}
