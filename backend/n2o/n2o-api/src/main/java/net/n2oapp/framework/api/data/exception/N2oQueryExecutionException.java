package net.n2oapp.framework.api.data.exception;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка выполнения запроса провайдера
 */
public class N2oQueryExecutionException extends N2oException {

    private String query;

    public N2oQueryExecutionException(String message, String query) {
        super(message);
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
