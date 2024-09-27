package net.n2oapp.framework.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.boot.graphql.N2oGraphQlException;

public class TestOperationExceptionHandler implements OperationExceptionHandler {

    private N2oException handleGraphQlException(Exception e) {
        DataSet result = ((N2oGraphQlException) e).getResponse();
        StringBuilder builder = new StringBuilder();
        DataSet errors = ((DataSet) result.getList("errors").get(0));

        String message = errors.getString("message");
        Integer column = errors.getInteger("column");
        Integer line = errors.getInteger("line");

        builder.append("Message: " + message + ", ");
        builder.append("line: " + line + ", ");
        builder.append("column: " + column + ".");

        N2oException exception = new N2oException();
        exception.setUserMessage(builder.toString());
        return exception;
    }

    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        if (e instanceof N2oGraphQlException)
            return handleGraphQlException(e);
        return new N2oException();
    }
}
