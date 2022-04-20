package net.n2oapp.framework.boot.graphql;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.exception.N2oQueryExecutionException;

/**
 * Ошибка обработки GraphQL запроса
 */
public class N2oGraphQlException extends N2oQueryExecutionException {

    @Getter
    private DataSet response;

    public N2oGraphQlException(DataSet result, String query) {
        super(query);
        this.response = result;
    }
}
