package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oClearAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
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
        String datasource = p.cast(source.getDatasourceId(), getLocalDatasourceId(p));
        clearAction.getPayload().setKey(getClientDatasourceId(datasource, p));
        if (Boolean.TRUE.equals(source.getCloseOnSuccess())) {
            if (clearAction.getMeta() == null)
                clearAction.setMeta(new MetaSaga());
            clearAction.getMeta().setModalsToClose(1);
        }
        return clearAction;
    }

    private String[] initPayloadPrefixes(N2oClearAction source, CompileProcessor p) {
        return source.getModel() != null ?
                source.getModel() :
                new String[]{getLocalModel(p).getId()};
    }
}
