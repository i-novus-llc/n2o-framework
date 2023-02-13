package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;


/**
 * Сборка компонента ButtonField
 */
@Component
public class ButtonFieldCompiler extends ActionFieldCompiler<ButtonField, N2oButtonField> {
    private static final String PROPERTY_PREFIX = "n2o.api.control.button_field";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButtonField.class;
    }

    @Override
    public ButtonField compile(N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p) {
        ButtonField field = new ButtonField();
        initDefaults(source, context, p);
        compileField(field, source, context, p);
        field.setColor(source.getColor());
        field.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));

        initItem(field, source, context, p);

        return field;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.button_field.src";
    }

    protected void initItem(ButtonField button, N2oButtonField source,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setProperties(p.mapAttributes(source));
        if (source.getType() != null && source.getType() == LabelType.ICON) {
            button.setLabel(null);
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType() == LabelType.TEXT) {
            button.setLabel(p.resolveJS(source.getLabel()));
        } else {
            button.setIcon(source.getIcon());
            button.setLabel(p.resolveJS(source.getLabel()));
        }
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
        if (LabelType.ICON.equals(source.getType()))
            hint = p.cast(source.getDescription(), source.getLabel());
        else
            hint = source.getDescription();
        if (hint != null) {
            button.setHint(p.resolveJS(hint.trim()));
            button.setHintPosition(source.getTooltipPosition());
        }

        if (source.getModel() == null)
            source.setModel(ReduxModel.resolve);

        compileConfirm(button, source, p, operation);

        String datasource = initDatasource(source, p);
        button.setValidate(compileValidate(source, p, datasource));
    }

    private List<String> compileValidate(N2oButtonField source, CompileProcessor p, String datasource) {
        if (!p.cast(source.getValidate(), datasource != null || source.getValidateDatasourceIds() != null))
            return null;
        if (source.getValidateDatasourceIds() != null)
            return Stream.of(source.getValidateDatasourceIds())
                    .map(ds -> getClientDatasourceId(ds, p))
                    .collect(Collectors.toList());
        if (datasource != null)
            return Collections.singletonList(getClientDatasourceId(datasource, p));
        throw new N2oException(String.format("validate-datasources is not defined in button [%s]", source.getId()));
    }

    private String initDatasource(N2oButtonField source, CompileProcessor p) {
        if (source.getDatasourceId() != null)
            return source.getDatasourceId();
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }

    private void compileConfirm(ButtonField button, N2oButtonField source,
                                   CompileProcessor p, CompiledObject.Operation operation) {
        boolean operationConfirm = operation != null && operation.getConfirm() != null && operation.getConfirm();
        if (source.getConfirm() != null) {
            Object condition = p.resolveJS(source.getConfirm(), Boolean.class);
            if (condition instanceof Boolean) {
                if (!((Boolean) condition || operationConfirm))
                    return;
                initConfirm(button, source, p, operation, true);
            }
            if (condition instanceof String) {
                 initConfirm(button, source, p, operation, condition);
            }
        }
        if (operationConfirm)
             initConfirm(button, source, p, operation, true);
    }

    private void initConfirm(ButtonField button, N2oButtonField source, CompileProcessor p, CompiledObject.Operation operation, Object condition) {
        Confirm confirm = new Confirm();
        confirm.setMode(p.cast(source.getConfirmType(), ConfirmType.MODAL));
        confirm.setTitle(p.cast(source.getConfirmTitle(), operation != null ? operation.getFormSubmitLabel() : null, p.getMessage("n2o.confirm.title")));
        confirm.setOk(new Confirm.Button(
                p.cast(source.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")),
                p.cast(source.getConfirmOkColor(), p.resolve(property("n2o.api.button.confirm.ok_color"), String.class))));
        confirm.setCancel(new Confirm.Button(
                p.cast(source.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")),
                p.cast(source.getConfirmCancelColor(), p.resolve(property("n2o.api.button.confirm.cancel_color"), String.class))));
        confirm.setText(initExpression(
                p.cast(source.getConfirmText(), operation != null ? operation.getConfirmationText() : null, p.getMessage("n2o.confirm.text"))));
        confirm.setCondition(initConfirmCondition(condition));
        confirm.setCloseButton(p.resolve(property("n2o.api.button.confirm.close_button"), Boolean.class));
        confirm.setReverseButtons(p.resolve(property("n2o.api.button.confirm.reverse_buttons"), Boolean.class));

        if (StringUtils.hasLink(confirm.getText())) {
            Set<String> links = StringUtils.collectLinks(confirm.getText());
            String text = Placeholders.js("'" + confirm.getText() + "'");
            for (String link : links) {
                text = text.replace(Placeholders.ref(link), "' + this." + link + " + '");
            }
            confirm.setText(text);
        }
        if (StringUtils.isJs(confirm.getText()) || StringUtils.isJs(confirm.getCondition())) {
            String datasource = initClientDatasourceId(source, p);
            confirm.setModelLink(new ModelLink(source.getModel(), datasource).getBindLink());
        }
        button.setConfirm(confirm);
    }

    protected String initClientDatasourceId(N2oButtonField source, CompileProcessor p) {
        if (source.getDatasourceId() != null)
            return getClientDatasourceId(source.getDatasourceId(), p);

        String datasourceId = initLocalDatasourceId(p);
        if (datasourceId != null)
            return getClientDatasourceId(datasourceId, p);
        else
            throw new N2oException(String.format("Unknown datasource for submit in field %s!", source.getId()));
    }

    private String initConfirmCondition(Object condition) {
        if (condition instanceof Boolean)
            return Placeholders.js(Boolean.toString(true));
        return initExpression((String) condition);
    }

    private String initExpression(String attr) {
        if (StringUtils.hasLink(attr)) {
            Set<String> links = StringUtils.collectLinks(attr);
            String text = js("'" + attr + "'");
            for (String link : links) {
                text = text.replace(ref(link), "' + this." + link + " + '");
            }
            return text;
        }
        return attr;
    }
}
