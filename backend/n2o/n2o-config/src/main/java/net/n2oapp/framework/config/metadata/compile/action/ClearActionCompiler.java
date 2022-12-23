package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oClearAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Сборка действия очистки модели
 */
@Component
public class ClearActionCompiler extends AbstractActionCompiler<ClearAction, N2oClearAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oClearAction.class;
    }

    @Override
    public ClearAction compile(N2oClearAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        ClearAction clearAction = new ClearAction();
        compileAction(clearAction, source, p);
        clearAction.setType(p.resolve(property("n2o.api.action.clear.type"), String.class));
        clearAction.getPayload().setPrefixes(initPayloadPrefixes(source, p));
        PageScope pageScope = p.getScope(PageScope.class);
        String datasource = p.cast(source.getDatasourceId(), getLocalDatasourceId(p));
        clearAction.getPayload().setKey(getClientDatasourceId(datasource, pageScope.getPageId(), p));
        if (Boolean.TRUE.equals(source.getCloseOnSuccess())) {
            if (clearAction.getMeta() == null)
                clearAction.setMeta(new MetaSaga());
            clearAction.getMeta().setModalsToClose(1);
        }
        return clearAction;
    }

    private String[] initPayloadPrefixes(N2oClearAction source, CompileProcessor p) {
        String[] prefixes;
        if (source.getModel() != null)
            prefixes = source.getModel();
        else {
            prefixes = new String[]{getLocalModel(p).getId()};
        }
        return prefixes;
    }
}
