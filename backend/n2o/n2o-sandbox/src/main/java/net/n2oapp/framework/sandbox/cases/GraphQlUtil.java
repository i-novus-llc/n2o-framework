package net.n2oapp.framework.sandbox.cases;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.boot.graphql.N2oGraphQlException;

/**
 * Утилитный класс для работы с кейсами, использующими GraphQl
 */
public class GraphQlUtil {

    /**
     * Сборка подробного сообщения об ошибке
     *
     * @param e Исключение GraphQl
     * @return Подробное сообщение об ошибке
     */
    public static N2oGraphQlException constructErrorMessage(N2oGraphQlException e) {
        DataSet response = e.getResponse();
        StringBuilder builder = new StringBuilder();
        DataSet errors = ((DataSet) response.getList("errors").get(0));

        String message = errors.getString("message");
        Integer column = ((DataSet) errors.getList("locations").get(0)).getInteger("column");
        Integer line = ((DataSet) errors.getList("locations").get(0)).getInteger("line");

        builder.append("Message: " + message + ", ");
        builder.append("line: " + line + " ");
        builder.append("column: " + column);

        return new N2oGraphQlException(builder.toString(), e.getQuery(), e.getResponse());
    }
}
