package net.n2oapp.framework.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.boot.graphql.GraphQlDataProviderEngine;

public class ConfiguredHandlerGraphQlDataProviderEngine extends GraphQlDataProviderEngine {

    @Override
    protected void errorHandler(DataSet result) {
        StringBuilder builder = new StringBuilder();
        DataSet errors = result.getDataSet("errors");

        String message = errors.getString("message");
        Integer line = errors.getInteger("line");
        Integer column = errors.getInteger("column");

        builder.append("Message: " + message + ", ");
        builder.append("line: " + line + ", ");
        builder.append("column: " + column + ".");

        throw new N2oUserException(builder.toString());
    }
}
