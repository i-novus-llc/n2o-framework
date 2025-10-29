package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oSubmitAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.submit.SubmitAction;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция действия сохранения источника данных
 */
@Component
public class SubmitActionCompiler extends AbstractActionCompiler<SubmitAction, N2oSubmitAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmitAction.class;
    }

    @Override
    public SubmitAction compile(N2oSubmitAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        SubmitAction submit = new SubmitAction();
        submit.setType(p.resolve(property("n2o.api.action.submit.type"), String.class));
        compileAction(submit, source, p);

        String datasourceId = source.getDatasourceId();
        if (datasourceId == null)
            datasourceId = getLocalDatasourceId(p);
        submit.getPayload().setDatasource(getClientDatasourceId(datasourceId, p));

        return submit;
    }
}
