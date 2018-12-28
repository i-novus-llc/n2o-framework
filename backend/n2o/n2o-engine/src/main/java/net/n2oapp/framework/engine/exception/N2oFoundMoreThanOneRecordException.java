package net.n2oapp.framework.engine.exception;

import net.n2oapp.framework.api.exception.N2oUserException;

/**
 * Найдено более одной записи
 */
public class N2oFoundMoreThanOneRecordException extends N2oUserException {

    public N2oFoundMoreThanOneRecordException() {
        super("n2o.summary.tooManyFound");
    }
}
