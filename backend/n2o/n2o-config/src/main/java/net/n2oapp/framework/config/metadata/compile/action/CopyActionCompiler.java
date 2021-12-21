package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil.getDatasourceByComponentScope;

/**
 * Сборка действия вызова операции
 */
@Component
public class CopyActionCompiler extends AbstractActionCompiler<CopyAction, N2oCopyAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCopyAction.class;
    }

    @Override
    public CopyAction compile(N2oCopyAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        CopyAction copyAction = new CopyAction();
        compileAction(copyAction, source, p);
        copyAction.setType(p.resolve(property("n2o.api.action.copy.type"), String.class));

        PageScope pageScope = p.getScope(PageScope.class);
        CopyActionPayload.ClientModel sourceModel = new CopyActionPayload.ClientModel(
                pageScope.getClientDatasourceId(source.getSourceDatasource()),
                source.getSourceModel().getId(), source.getSourceFieldId());
        CopyActionPayload.ClientModel targetModel = new CopyActionPayload.ClientModel(
                pageScope.getClientDatasourceId(source.getTargetDatasource()),
                source.getTargetModel().getId(),
                source.getTargetFieldId());

        copyAction.getPayload().setSource(sourceModel);
        copyAction.getPayload().setTarget(targetModel);
        copyAction.getPayload().setMode(source.getMode());

        copyAction.setMeta(compileMeta(p));
        return copyAction;
    }

    @Override
    protected void initDefaults(N2oCopyAction source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setMode(p.cast(source.getMode(), CopyMode.merge));
        source.setSourceModel(p.cast(source.getSourceModel(), ReduxModel.RESOLVE));
        source.setSourceDatasource(initSourceDatasource(source, p));
        source.setTargetModel(p.cast(source.getTargetModel(), ReduxModel.RESOLVE));
        source.setTargetDatasource(initTargetDatasource(source, p));
    }

    private MetaSaga compileMeta(CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        Boolean closeLastModal = p.resolve(property("n2o.api.action.copy.close_on_success"), Boolean.class);
        meta.setModalsToClose(closeLastModal ? 1 : 0);
        return meta;
    }

    private String initSourceDatasource(N2oCopyAction source, CompileProcessor p) {
        if (source.getSourceDatasource() != null)
            return source.getSourceDatasource();
        String datasource = getDatasourceByComponentScope(p);
        if (datasource != null)
            return datasource;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        throw new N2oException(String.format("source-datasource is not undefined in copy action [%s]", source.getId()));
    }

    private String initTargetDatasource(N2oCopyAction source, CompileProcessor p) {
        if (source.getTargetDatasource() != null)
            return source.getTargetDatasource();
        return initSourceDatasource(source, p);
    }
}
