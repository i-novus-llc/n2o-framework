package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ButtonCondition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция ToolbarItem
 * @param <S>
 */
public abstract class BaseButtonCompiler<S extends GroupItem, B extends AbstractButton> implements BaseSourceCompiler<B, S, CompileContext<?, ?>> {

    protected void initItem(MenuItem button, AbstractMenuItem source, IndexScope idx,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setId(castDefault(source.getId(), source.getActionId(), "menuItem" + idx.get()));
        source.setId(button.getId());
        button.setProperties(p.mapAttributes(source));
        if (source.getType() != null && source.getType().equals(LabelType.icon)) {
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType().equals(LabelType.text)) {
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
            MetaActions metaActions = p.getScope(MetaActions.class);
            action = metaActions.get(source.getActionId());
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
            }
            //todo если это invoke-action, то из action в объекте должны доставаться поля action.getName(), confirmationText
        }

        initConfirm(button, source, context, p, operation);
        button.setClassName(source.getClassName());
        button.setStyle(StylesResolver.resolveStyles(source.getStyle()));

        String hint;
        if (LabelType.icon.equals(source.getType()))
            hint = p.cast(source.getDescription(), source.getLabel());
        else
            hint = source.getDescription();

        if (hint != null) {
            button.setHint(hint.trim());
            if (source.getTooltipPosition() != null) {
                button.setHintPosition(source.getTooltipPosition());
            } else {
                button.setHintPosition(
                        source instanceof N2oButton
                                ? p.resolve(property("n2o.api.button.tooltip_position"), String.class)
                                : p.resolve(property("n2o.api.menuitem.tooltip_position"), String.class)
                );
            }
        }

        button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        if (source.getModel() == null)
            source.setModel(ReduxModel.RESOLVE);
        compileDependencies(button, source, context, p);
        button.setValidate(source.getValidate());
    }

    private void initConfirm(MenuItem button, AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p, CompiledObject.Operation operation) {
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

    protected String initWidgetId(AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getWidgetId() != null) {
            return pageScope == null ? source.getWidgetId() : pageScope.getGlobalWidgetId(source.getWidgetId());//todo обсудить
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

    /**
     * Компиляция зависимостей между полем и кнопкой
     *
     * @param button клиентская модель кнопки
     * @param source исходная модель поля
     */
    protected void compileDependencies(MenuItem button, AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getVisibilityConditions() != null) {
            String widgetId = initWidgetId(source, context, p);
            List<ButtonCondition> conditions = new ArrayList<>();
            for (N2oButtonCondition n2oCondition : source.getVisibilityConditions()) {
                ButtonCondition condition = new ButtonCondition();
                condition.setExpression(n2oCondition.getExpression().trim());
                condition.setModelLink(new ModelLink(source.getModel(), widgetId).getBindLink());
                conditions.add(condition);
            }
            button.getConditions().put(ValidationType.visible, conditions);
        }
        List<ButtonCondition> conditions = new ArrayList<>();
        if (source.getEnablingConditions() != null) {
            String widgetId = initWidgetId(source, context, p);
            for (N2oButtonCondition n2oCondition : source.getEnablingConditions()) {
                ButtonCondition condition = new ButtonCondition();
                condition.setExpression(n2oCondition.getExpression().trim());
                condition.setModelLink(new ModelLink(source.getModel(), widgetId).getBindLink());
                conditions.add(condition);
            }
        }
        if (source.getModel() == null || source.getModel().equals(ReduxModel.RESOLVE)) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope == null || componentScope.unwrap(N2oCell.class) == null) {
                String widgetId = initWidgetId(source, context, p);
                ButtonCondition condition = new ButtonCondition();
                condition.setExpression("!_.isEmpty(this)");
                condition.setModelLink(new ModelLink(ReduxModel.RESOLVE, widgetId).getBindLink());
                conditions.add(condition);
            }
        }
        if (!conditions.isEmpty()) {
            button.getConditions().put(ValidationType.enabled, conditions);
        }
    }
}
