package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.MergeMode;
import net.n2oapp.framework.api.metadata.event.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueActionPayload;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
        SetValueAction setValueAction = new SetValueAction();
        compileAction(setValueAction, source, p);
        setValueAction.setType(p.resolve(property("n2o.api.action.copy.type"), String.class));

        String defaultWidgetId = initTargetWidget(context, p);
        ReduxModel model = getTargetWidgetModel(p, ReduxModel.RESOLVE);
        PageScope pageScope = p.getScope(PageScope.class);
        String sourceWidgetId = source.getSourceWidget() == null ? defaultWidgetId :
                pageScope.getGlobalWidgetId(source.getSourceWidget());
        SetValueActionPayload.ClientModel sourceModel = new SetValueActionPayload.ClientModel(
                pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null ? sourceWidgetId
                        : pageScope.getWidgetIdClientDatasourceMap().get(sourceWidgetId),
                p.cast(source.getSourceModel(), model.getId()));
        String targetWidgetId = source.getTargetWidget() == null ? defaultWidgetId :
                pageScope.getGlobalWidgetId(source.getTargetWidget());
        SetValueActionPayload.ClientModel targetModel = new SetValueActionPayload.ClientModel(
                pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null ? targetWidgetId
                        : pageScope.getWidgetIdClientDatasourceMap().get(targetWidgetId),
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
