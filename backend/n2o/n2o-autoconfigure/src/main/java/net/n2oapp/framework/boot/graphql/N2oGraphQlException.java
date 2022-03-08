package net.n2oapp.framework.boot.graphql;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка обработки GraphQL запроса
 */
public class N2oGraphQlException extends N2oException {

    public N2oGraphQlException(String message) {
        super(message);
    }
}
