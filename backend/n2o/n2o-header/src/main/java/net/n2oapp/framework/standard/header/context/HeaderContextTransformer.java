package net.n2oapp.framework.standard.header.context;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.transformer.CompileTransformer;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.standard.header.model.global.context.UserContextStructure;
import net.n2oapp.framework.standard.header.model.local.CompiledStandardHeader;
import net.n2oapp.framework.standard.header.model.local.CompiledStandardHeader.CompiledUserMenu;

import java.util.Collection;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * User: operhod
 * Date: 19.05.14
 * Time: 16:56
 */
@Deprecated
public class HeaderContextTransformer implements CompileTransformer<CompiledStandardHeader, CompileContext> {

    private static final UserContextModelBuilder USER_CONTEXT_MODEL_BUILDER = new UserContextModelBuilder();

    private QueryProcessor queryProcessor;

    public HeaderContextTransformer() {
        this.queryProcessor = getBean(QueryProcessor.class);
    }

    @Override
    public CompiledStandardHeader transformBeforeMap(CompiledStandardHeader header, CompileContext compileContext, UserContext user) {
        UserContextStructure context = header.getUserContextStructure();
        if (context != null) {
//            CompiledQuery query = CompilerHolder.get().get(context.getQueryId(), CompiledQuery.class);
//            Collection<DataSet> contextData = queryProcessor.execute(query, N2oPreparedCriteria.simpleCriteria(query, context.getUserFieldId(),
//                            user.get(context.getUserFieldId()))).getCollection();
//            header.setUserContext(USER_CONTEXT_MODEL_BUILDER.build(context, contextData));
            //todo:закоментировано поскольку изменится компиляция
        }
        CompiledUserMenu userMenu = header.getUserMenu();
        if (userMenu != null) {
            String username = (String) user.get(header.getUserMenu().getUsernameContext());
            header.getUserMenu().setUsername(username);
        }
        return header;
    }

    @Override
    public Class<CompiledStandardHeader> getCompiledMetadataClass() {
        return CompiledStandardHeader.class;
    }

    @Override
    public Class<CompileContext> getContextClass() {
        return CompileContext.class;
    }

}
