package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.MergeModeEnum;
import net.n2oapp.framework.api.metadata.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueActionPayload;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.widget.ModelLinkUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.getLocalDatasourceId;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.getLocalModel;
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

        String sourceDatasourceId = castDefault(source.getSourceDatasourceId(), () -> getLocalDatasourceId(p));
        String targetDatasourceId = castDefault(source.getTargetDatasourceId(), () -> getLocalDatasourceId(p));
        ReduxModelEnum model = getLocalModel(p);

        String clientSourceDatasourceId = getClientDatasourceId(sourceDatasourceId, p);
        SetValueActionPayload.ClientModel sourceModel = new SetValueActionPayload.ClientModel(clientSourceDatasourceId,
                castDefault(source.getSourceModel(), model.getId()));
        String clientTargetDatasourceId = getClientDatasourceId(targetDatasourceId, p);
        if (clientTargetDatasourceId == null) {
            throw new N2oException("В действии \"<set-value>\" не задан атрибут 'target-datasource'");
        }
        SetValueActionPayload.ClientModel targetModel = new SetValueActionPayload.ClientModel(clientTargetDatasourceId,
                castDefault(source.getTargetModel(), model.getId()));
        targetModel.setField(ModelLinkUtil.getField(source.getTo(), targetDatasourceId, p));
        setValueAction.getPayload().setSource(sourceModel);
        setValueAction.getPayload().setTarget(targetModel);

        setValueAction.getPayload().setSourceMapper(ScriptProcessor.resolveFunction(source.getExpression()));
        setValueAction.getPayload().setMode(castDefault(source.getMergeMode(), MergeModeEnum.REPLACE));

        return setValueAction;
    }
}
