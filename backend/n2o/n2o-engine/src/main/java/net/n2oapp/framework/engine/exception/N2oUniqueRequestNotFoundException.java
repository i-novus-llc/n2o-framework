package net.n2oapp.framework.engine.exception;

import net.n2oapp.framework.api.exception.N2oUserException;

/**
 * Не найден <unique> запрос
 */
public class N2oUniqueRequestNotFoundException extends N2oUserException {

    public N2oUniqueRequestNotFoundException(String queryId) {
        super("n2o.summary.uniqueRequestNotFound");
        setData(queryId);
    }


}