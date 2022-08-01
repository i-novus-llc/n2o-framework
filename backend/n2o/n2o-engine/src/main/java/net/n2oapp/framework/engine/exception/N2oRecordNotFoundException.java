package net.n2oapp.framework.engine.exception;

import net.n2oapp.framework.api.exception.N2oUserException;

/**
 * Запись не найдена
 */
public class N2oRecordNotFoundException extends N2oUserException {

    public N2oRecordNotFoundException() {
        super("n2o.summary.nothingFound");
    }

    public N2oRecordNotFoundException(Throwable e) {
        super("n2o.summary.nothingFound", e);
    }
}
