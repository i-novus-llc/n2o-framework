package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция действия обновления данных виджета
 */
@Component
public class RefreshActionCompiler extends AbstractActionCompiler<RefreshAction, N2oRefreshAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRefreshAction.class;
    }

    @Override
    public RefreshAction compile(N2oRefreshAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        RefreshAction refreshAction = new RefreshAction();
        compileAction(refreshAction, source, p);
        refreshAction.setType(p.resolve(property("n2o.api.action.refresh.type"), String.class));
        String clientDatasource = getClientDsId(source, context, p);
        ((RefreshPayload) refreshAction.getPayload()).setDatasource(clientDatasource);
        return refreshAction;
    }

    @Override
    protected void initDefaults(N2oRefreshAction source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setDatasourceId(p.cast(source.getDatasourceId(), getLocalDatasourceId(p)));
    }

    private String getClientDsId(N2oRefreshAction source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getPage() == PageRef.PARENT && context instanceof PageContext) {
            Map<String, String> parentDatasourceIdsMap = ((PageContext) context).getParentDatasourceIdsMap();
            if (parentDatasourceIdsMap != null && parentDatasourceIdsMap.containsKey(source.getDatasourceId()))
                return parentDatasourceIdsMap.get(source.getDatasourceId());
            return getClientDatasourceId(source.getDatasourceId(), ((PageContext) context).getParentClientPageId());
        }
        return getClientDatasourceId(source.getDatasourceId(), p);
    }
}
