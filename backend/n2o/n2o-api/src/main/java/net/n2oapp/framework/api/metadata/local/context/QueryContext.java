package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;

/**
 * User: iryabov
 * Date: 13.12.13
 * Time: 15:09
 */
@Deprecated
public abstract class QueryContext<P extends CompileContext> extends BaseCompileContext<N2oQuery, P> {
    protected QueryContext(P parentContext, N2oQuery metadata, ContextMetadataProvider<N2oQuery, P> provider) {
        super(parentContext, metadata, provider);
    }

    @Override
    public Class<N2oQuery> getMetadataClassByContext() {
        return N2oQuery.class;
    }


}
