package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.action.MergeMode;
import net.n2oapp.framework.api.metadata.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueActionPayload;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Сборка действия set-value
 */
@Component
public class SetValueActionCompiler extends AbstractActionCompiler<SetValueAction, N2oSetValueAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSetValueAction.class;
    }

    @Override
    public SetValueAction compile(N2oSetValueAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        SetValueAction setValueAction = new SetValueAction();
        compileAction(setValueAction, source, p);
        setValueAction.setType(p.resolve(property("n2o.api.action.copy.type"), String.class));

        String defaultDatasource = getClientDatasourceId(getLocalDatasourceId(p), p);
        ReduxModel model = getModelFromComponentScope(p);
        String sourceDatasourceId = source.getSourceDatasourceId() == null ? defaultDatasource :
                getClientDatasourceId(source.getSourceDatasourceId(), p);
        SetValueActionPayload.ClientModel sourceModel = new SetValueActionPayload.ClientModel(sourceDatasourceId,
                p.cast(source.getSourceModel(), model.getId()));
        String targetDatasourceId = source.getTargetDatasourceId() == null ? defaultDatasource :
                getClientDatasourceId(source.getTargetDatasourceId(), p);
        SetValueActionPayload.ClientModel targetModel = new SetValueActionPayload.ClientModel(targetDatasourceId,
                p.cast(source.getTargetModel(), model.getId()));
        targetModel.setField(source.getTo());
        setValueAction.getPayload().setSource(sourceModel);
        setValueAction.getPayload().setTarget(targetModel);

        setValueAction.getPayload().setSourceMapper(toJS(source.getExpression()));
        setValueAction.getPayload().setMode(p.cast(source.getMergeMode(), MergeMode.replace));

        return setValueAction;
    }

    private String toJS(String value) {
        return value == null ? null : "`" + value.trim() + "`";
    }
}
