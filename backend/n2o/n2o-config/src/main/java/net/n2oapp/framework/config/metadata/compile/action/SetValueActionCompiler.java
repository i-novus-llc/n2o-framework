package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.MergeModeEnum;
import net.n2oapp.framework.api.metadata.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueActionPayload;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
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
        initDefaults(source, p);
        SetValueAction setValueAction = new SetValueAction();
        compileAction(setValueAction, source, p);
        setValueAction.setType(p.resolve(property("n2o.api.action.copy.type"), String.class));
        setValueAction.setValidate(castDefault(source.getValidate(),
                () -> p.resolve(property("n2o.api.action.set_value.validate"), Boolean.class)));

        String defaultDatasource = getClientDatasourceId(getLocalDatasourceId(p), p);
        ReduxModelEnum model = getModelFromComponentScope(p);

        String sourceDatasourceId = source.getSourceDatasourceId() == null ? defaultDatasource :
                getClientDatasourceId(source.getSourceDatasourceId(), p);
        SetValueActionPayload.ClientModel sourceModel = new SetValueActionPayload.ClientModel(sourceDatasourceId,
                castDefault(source.getSourceModel(), model.getId()));
        String targetDatasourceId = source.getTargetDatasourceId() == null ? defaultDatasource :
                getClientDatasourceId(source.getTargetDatasourceId(), p);
        SetValueActionPayload.ClientModel targetModel = new SetValueActionPayload.ClientModel(targetDatasourceId,
                castDefault(source.getTargetModel(), model.getId()));
        targetModel.setField(source.getTo());
        setValueAction.getPayload().setSource(sourceModel);
        setValueAction.getPayload().setTarget(targetModel);

        setValueAction.getPayload().setSourceMapper(ScriptProcessor.resolveFunction(source.getExpression()));
        setValueAction.getPayload().setMode(castDefault(source.getMergeMode(), MergeModeEnum.REPLACE));

        return setValueAction;
    }
}