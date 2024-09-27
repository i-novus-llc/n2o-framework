package net.n2oapp.framework.sandbox.cases;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.boot.graphql.N2oGraphQlException;
import org.springframework.stereotype.Component;

@Component
public class SandboxOperationExceptionHandler implements OperationExceptionHandler {
    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        if (e instanceof N2oGraphQlException graphQlException)
            return GraphQlUtil.constructErrorMessage(graphQlException);

        if (e instanceof N2oException n2oException) {
            return addFailInfo(o, data, n2oException);
        } else
            return addFailInfo(o, data, new N2oException(e));
    }

    private N2oException addFailInfo(CompiledObject.Operation o, DataSet data, N2oException e) {
        if (o.getFailText() != null) {
            e.setUserMessage(StringUtils.resolveLinks(o.getFailText(), data));
        }
        if (o.getFailTitle() != null)
            e.setUserMessageTitle(StringUtils.resolveLinks(o.getFailTitle(), data));
        return e;
    }
}