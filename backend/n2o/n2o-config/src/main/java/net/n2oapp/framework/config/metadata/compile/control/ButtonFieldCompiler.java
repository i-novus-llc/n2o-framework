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
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Сборка компонента ButtonField
 */
@Component
public class ButtonFieldCompiler extends ActionFieldCompiler<ButtonField, N2oButtonField> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButtonField.class;
    }

    @Override
    public ButtonField compile(N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p) {
        ButtonField field = new ButtonField();
        compileField(field, source, context, p);
        field.setColor(source.getColor());

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
        if (source.getType() != null && source.getType() == LabelType.icon) {
            button.setLabel(null);
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType() == LabelType.text) {
            button.setLabel(source.getLabel());
        } else {
            button.setIcon(source.getIcon());
            button.setLabel(source.getLabel());
        }
        CompiledObject.Operation operation = null;
        Action action = compileAction(source, button, context, p);

        if (action != null) {
            button.setAction(action);
            if (action instanceof InvokeAction) {
                String objectId = ((InvokeAction) action).getObjectId() == null ? source.getWidgetId() :
                        ((InvokeAction) action).getObjectId();
                CompiledObject compiledObject = getCompiledObject(p, objectId);
                operation = compiledObject != null && compiledObject.getOperations() != null
                        && compiledObject.getOperations().containsKey(((InvokeAction) action).getOperationId()) ?
                        compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;
            }
        }

        initConfirm(button, source, context, p, operation);

        String hint;
        if (LabelType.icon.equals(source.getType()))
            hint = p.cast(source.getDescription(), source.getLabel());
        else
            hint = source.getDescription();

        if (hint != null) {
            button.setHint(hint.trim());
            button.setHintPosition(source.getTooltipPosition());
        }

        if (source.getModel() == null)
            source.setModel(ReduxModel.resolve);

        String datasource = initDatasource(source, p);
        button.setValidate(compileValidate(source, p, datasource));
    }

    private List<String> compileValidate(N2oButtonField source, CompileProcessor p, String datasource) {
        if (!p.cast(source.getValidate(), datasource != null || source.getValidateDatasources() != null))
            return null;
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getValidateDatasources() != null)
            return Stream.of(source.getValidateDatasources())
                    .map(ds -> pageScope != null ? pageScope.getClientDatasourceId(ds) : ds)
                    .collect(Collectors.toList());
        if (datasource != null) {
            String ds = pageScope == null ? datasource : pageScope.getClientDatasourceId(datasource);
            return Arrays.asList(ds);
        }
        throw new N2oException(String.format("validate-datasources is not defined in button [%s]", source.getId()));
    }

    private String initDatasource(N2oButtonField source, CompileProcessor p) {
        if (source.getDatasource() != null)
            return source.getDatasource();
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }

    private void initConfirm(ButtonField button, N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p, CompiledObject.Operation operation) {
        if ((source.getConfirm() == null || !source.getConfirm()) &&
                (source.getConfirm() != null || operation == null || operation.getConfirm() == null || !operation.getConfirm()))
            return;
        Confirm confirm = new Confirm();
        confirm.setMode(p.cast(source.getConfirmType(), ConfirmType.modal));
        confirm.setText(p.cast(source.getConfirmText(), (operation != null ? operation.getConfirmationText() : null), p.getMessage("n2o.confirm.text")));
        confirm.setTitle(p.cast(source.getConfirmTitle(), (operation != null ? operation.getFormSubmitLabel() : null), p.getMessage("n2o.confirm.title")));
        confirm.setOkLabel(p.cast(source.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")));
        confirm.setCancelLabel(p.cast(source.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")));
        if (StringUtils.hasLink(confirm.getText())) {
            Set<String> links = StringUtils.collectLinks(confirm.getText());
            String text = Placeholders.js("'" + confirm.getText() + "'");
            for (String link : links) {
                text = text.replace(Placeholders.ref(link), "' + this." + link + " + '");
            }
            confirm.setText(text);
        }
        if (StringUtils.isJs(confirm.getText())) {
            String datasource = initGlobalDatasourceId(source, context, p);
            ReduxModel reduxModel = source.getModel();
            confirm.setModelLink(new ModelLink(reduxModel == null ? ReduxModel.resolve : reduxModel, datasource).getBindLink());
        }
        button.setConfirm(confirm);
    }

    protected String initGlobalDatasourceId(N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getDatasource() != null) {
            return pageScope != null ? pageScope.getClientDatasourceId(source.getDatasource()) : source.getDatasource();
        }
        String datasourceId = initLocalDatasourceId(p);
        if (datasourceId != null)
            return pageScope != null ? pageScope.getClientDatasourceId(datasourceId) : datasourceId;
        else
            throw new N2oException(String.format("Unknown datasource for submit in field %s!", source.getId()));
    }
}
