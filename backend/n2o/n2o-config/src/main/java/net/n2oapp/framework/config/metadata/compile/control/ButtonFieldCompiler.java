package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.util.DatasourceUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.*;


/**
 * Сборка компонента ButtonField
 */
@Component
public class ButtonFieldCompiler extends ActionFieldCompiler<ButtonField, N2oButtonField> {
    private static final String PROPERTY_PREFIX = "n2o.api.control.button";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButtonField.class;
    }

    @Override
    public ButtonField compile(N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p) {
        ButtonField field = new ButtonField();
        initDefaults(source, context, p);
        compileField(field, source, context, p);
        field.setColor(p.resolveJS(source.getColor()));
        field.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));
        field.setRounded(castDefault(source.getRounded(),
                () -> p.resolve(property("n2o.api.control.button.rounded"), Boolean.class)));
        initItem(field, source, context, p);
        return field;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.button.src";
    }

    protected void initItem(ButtonField button, N2oButtonField source,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setProperties(p.mapAttributes(source));
        button.setIcon(source.getIcon());
        button.setLabel(p.resolveJS(source.getLabel()));
        CompiledObject.Operation operation = null;
        Action action = compileAction(source, button, context, p);

        if (action instanceof InvokeAction) {
            String objectId = ((InvokeAction) action).getObjectId() == null ? source.getWidgetId() :
                    ((InvokeAction) action).getObjectId();
            CompiledObject compiledObject = getCompiledObject(p, objectId);
            operation = compiledObject != null && compiledObject.getOperations() != null
                    && compiledObject.getOperations().containsKey(((InvokeAction) action).getOperationId()) ?
                    compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;
        }

        String hint;
        hint = source.getDescription();
        if (hint != null) {
            button.setHint(p.resolveJS(hint.trim()));
            button.setHintPosition(source.getTooltipPosition());
        }

        if (source.getModel() == null)
            source.setModel(ReduxModel.resolve);

        String datasource = initDatasource(source, p);
        boolean validate = initValidate(source, datasource);
        source.setValidate(validate);
        button.setValidate(compileValidate(source, p, datasource));
        button.setDatasource(DatasourceUtil.getClientDatasourceId(datasource, p));
    }
}
