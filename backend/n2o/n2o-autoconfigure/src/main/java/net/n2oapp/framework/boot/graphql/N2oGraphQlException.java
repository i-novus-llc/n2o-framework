package net.n2oapp.framework.boot.graphql;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка обработки GraphQL запроса
 */
public class N2oGraphQlException extends N2oException {

    @Getter
    private DataSet response;

    public N2oGraphQlException(DataSet result) {
        this.response = result;
    }
}
