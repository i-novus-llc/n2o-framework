package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
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
        initDefaults(source, p);
        RefreshAction refreshAction = new RefreshAction();
        compileAction(refreshAction, source, p);
        refreshAction.setType(p.resolve(property("n2o.api.action.refresh.type"), String.class));
        String clientDatasource = getClientDatasourceId(source.getDatasourceId(), p);
        ((RefreshPayload) refreshAction.getPayload()).setDatasource(clientDatasource);
        return refreshAction;
    }

    @Override
    protected void initDefaults(N2oRefreshAction source, CompileProcessor p) {
        super.initDefaults(source, p);
        source.setDatasourceId(castDefault(source.getDatasourceId(), () -> getLocalDatasourceId(p)));
    }
}
