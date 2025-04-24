package net.n2oapp.framework.sandbox.cases;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.QueryExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.boot.graphql.N2oGraphQlException;
import org.springframework.stereotype.Component;

@Component
public class SandboxQueryExceptionHandler implements QueryExceptionHandler {

    @Override
    public N2oException handle(CompiledQuery query, N2oPreparedCriteria criteria, Exception e) {
        if (e instanceof N2oGraphQlException graphQlException)
            return GraphQlUtil.constructErrorMessage(graphQlException);
        if (e instanceof N2oException n2oException)
            return n2oException;
        return new N2oException(e);
    }
}
