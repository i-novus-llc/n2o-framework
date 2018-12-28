package net.n2oapp.framework.engine.rest;

import net.n2oapp.framework.api.exception.ForeignStackTraceAware;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.UserMessageAware;

/**
 * Исключение с внешним стектрейсом
 */
public class N2oExceptionWithForeignStackTrace extends N2oException implements ForeignStackTraceAware {

    private String stackTrace;

    public N2oExceptionWithForeignStackTrace() {
    }

    public N2oExceptionWithForeignStackTrace(Throwable cause) {
        super(cause);
    }

    public N2oExceptionWithForeignStackTrace(String stackTrace) {
        super();
        this.stackTrace = stackTrace;
    }

    @Override
    public String getForeignStackTrace() {
        return stackTrace;
    }

}
