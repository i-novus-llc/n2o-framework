package net.n2oapp.framework.boot.graphql;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка обработки GraphQL запроса
 */
public class N2oGraphqlException extends N2oException {

    public N2oGraphqlException(String message) {
        super(message);
    }
}
