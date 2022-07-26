package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil.getDatasourceByComponentScope;
import static net.n2oapp.framework.config.util.CompileUtil.getClientDatasourceId;

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

        CopyActionPayload.ClientModel sourceModel = new CopyActionPayload.ClientModel(
                getClientDatasourceId(source.getSourceDatasourceId(), p),
                source.getSourceModel().getId(), source.getSourceFieldId());
        CopyActionPayload.ClientModel targetModel = new CopyActionPayload.ClientModel(
                getClientTargetDatasourceId(source, context, p),
                source.getTargetModel().getId(),
                source.getTargetFieldId());

        copyAction.getPayload().setSource(sourceModel);
        copyAction.getPayload().setTarget(targetModel);
        copyAction.getPayload().setMode(source.getMode());

        copyAction.setMeta(compileMeta(source, p));
        return copyAction;
    }

    @Override
    protected void initDefaults(N2oCopyAction source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setMode(p.cast(source.getMode(), CopyMode.merge));
        source.setSourceModel(p.cast(source.getSourceModel(), ReduxModel.resolve));
        source.setSourceDatasourceId(initSourceDatasourceId(source, p));
        source.setTargetModel(p.cast(source.getTargetModel(), ReduxModel.resolve));
        source.setTargetDatasourceId(initTargetDatasourceId(source, p));
    }

    private MetaSaga compileMeta(N2oCopyAction source, CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        boolean closeOnSuccess = p.cast(source.getCloseOnSuccess(),
                p.resolve(property("n2o.api.action.copy.close_on_success"), Boolean.class));
        meta.setModalsToClose(closeOnSuccess ? 1 : 0);
        return meta;
    }

    private String initSourceDatasourceId(N2oCopyAction source, CompileProcessor p) {
        if (source.getSourceDatasourceId() != null)
            return source.getSourceDatasourceId();
        String datasource = getDatasourceByComponentScope(p);
        if (datasource != null)
            return datasource;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        throw new N2oException(String.format("source-datasource is not undefined in copy action [%s]", source.getId()));
    }

    private String initTargetDatasourceId(N2oCopyAction source, CompileProcessor p) {
        if (source.getTargetDatasourceId() != null)
            return source.getTargetDatasourceId();
        return initSourceDatasourceId(source, p);
    }

    private String getClientTargetDatasourceId(N2oCopyAction source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getTargetClientPageId() != null)
            return getClientDatasourceId(source.getTargetDatasourceId(), source.getTargetClientPageId());
        else if (PageRef.PARENT.equals(source.getTargetPage())) {
            if (context instanceof PageContext)
                return getClientDatasourceId(source.getTargetDatasourceId(), ((PageContext) context).getParentClientPageId());
            else
                throw new N2oException("Action 'copy' has target-page=\"parent\" but PageContext not found");
        }
        return getClientDatasourceId(source.getTargetDatasourceId(), p);
    }
}
