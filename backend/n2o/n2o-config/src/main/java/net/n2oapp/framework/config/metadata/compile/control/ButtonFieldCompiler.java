package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * Сборка компонента ButtonField
 */
@Component
public class ButtonFieldCompiler extends FieldCompiler<ButtonField, N2oButtonField> {

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

        if (source.getValidate() != null && source.getValidate()) {
            field.setValidatedWidgetId(initWidgetId(source, context, p));
        }
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
        Action action = null;

        CompiledObject compiledObject;
        WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);
        if (widgetObjectScope != null && widgetObjectScope.containsKey(source.getWidgetId())) {
            compiledObject = widgetObjectScope.getObject(source.getWidgetId());
        } else
            compiledObject = p.getScope(CompiledObject.class);

        if (source.getActionId() != null) {
            ComponentScope scope = p.getScope(ComponentScope.class);
            N2oAction act = getAction(scope, source.getActionId());
            if (act != null) {
                act.setId(button.getId());
                action = p.compile(act, context, compiledObject, scope);
            }
        } else {
            N2oAction butAction = source.getAction();
            if (butAction != null) {
                butAction.setId(p.cast(butAction.getId(), button.getId()));
                action = p.compile(butAction, context, compiledObject, new ComponentScope(source));
            }
        }

        if (action != null) {
            button.setAction(action);
            if (action instanceof InvokeAction) {
                operation = compiledObject != null && compiledObject.getOperations() != null
                        && compiledObject.getOperations().containsKey(((InvokeAction) action).getOperationId()) ?
                        compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;
            } else if (action instanceof LinkAction) {
                LinkAction linkAction = ((LinkAction) button.getAction());
                button.setUrl(linkAction.getUrl());
                button.setTarget(linkAction.getTarget());
                button.setPathMapping(linkAction.getPathMapping());
                button.setQueryMapping(linkAction.getQueryMapping());
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
            source.setModel(ReduxModel.RESOLVE);
        button.setValidate(source.getValidate());
    }

    private N2oAction getAction(ComponentScope scope, String actionId) {
        if (scope != null) {
            N2oForm form = scope.unwrap(N2oForm.class);
            if (form != null && form.getActions() != null) {
                for (ActionsBar act : form.getActions()) {
                    if (actionId.equals(act.getId()))
                        return act.getAction();
                }
            }
        }
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
            String widgetId = initWidgetId(source, context, p);
            ReduxModel reduxModel = source.getModel();
            confirm.setModelLink(new ModelLink(reduxModel == null ? ReduxModel.RESOLVE : reduxModel, widgetId).getBindLink());
        }
        button.setConfirm(confirm);
    }

    protected String initWidgetId(N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getWidgetId() != null) {
            return pageScope == null ? source.getWidgetId() : pageScope.getGlobalWidgetId(source.getWidgetId());
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            return widgetScope.getClientWidgetId();
        } else if (context instanceof PageContext && ((PageContext) context).getResultWidgetId() != null) {
            return pageScope.getGlobalWidgetId(((PageContext) context).getResultWidgetId());
        } else {
            throw new N2oException("Unknown widgetId for invoke action!");
        }
    }
}
