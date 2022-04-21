package net.n2oapp.framework.api.data.exception;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка выполнения запроса провайдера
 */
public class N2oQueryExecutionException extends N2oException {

    private static final String DEFAULT_MESSAGE = "Query execution error";
    private String query;

    public N2oQueryExecutionException(String query) {
        super(DEFAULT_MESSAGE);
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
