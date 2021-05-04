package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
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
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.api.script.ScriptProcessor;
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

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;

/**
 * Компиляция ToolbarItem
 */
public abstract class BaseButtonCompiler<S extends GroupItem, B extends AbstractButton> implements BaseSourceCompiler<B, S, CompileContext<?, ?>> {

    protected void initItem(MenuItem button, AbstractMenuItem source, IndexScope idx,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setId(p.cast(source.getId(), source.getActionId(), "menuItem" + idx.get()));
        source.setId(button.getId());
        button.setProperties(p.mapAttributes(source));
        if (source.getType() != null && source.getType() == LabelType.icon) {
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType() == LabelType.text) {
            button.setLabel(source.getLabel());
        } else {
            button.setIcon(source.getIcon());
            button.setLabel(source.getLabel());
        }
        CompiledObject.Operation operation = null;
        CompiledObject compiledObject = null;
        WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);
        if (widgetObjectScope != null) {
            if (widgetObjectScope.size() == 1 && source.getWidgetId() == null)
                source.setWidgetId(widgetObjectScope.keySet().iterator().next());
            if (widgetObjectScope.containsKey(source.getWidgetId())) {
                compiledObject = widgetObjectScope.getObject(source.getWidgetId());
            }
        }
        if (compiledObject == null)
            compiledObject = p.getScope(CompiledObject.class);
        Action action = compileAction(button, source, context, p, compiledObject);

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

        if (source.getModel() == null)
            source.setModel(ReduxModel.RESOLVE);
        compileConditionsAndDependencies(button, source, context, p);
        button.setValidate(source.getValidate());
    }

    private Action compileAction(MenuItem button, AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p,
                                 CompiledObject compiledObject) {
        Action action = null;
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
        return action;
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
            String text = js("'" + confirm.getText() + "'");
            for (String link : links) {
                text = text.replace(ref(link), "' + this." + link + " + '");
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
     * Компиляция условий и зависимостей между полем и кнопкой
     *
     * @param button клиентская модель кнопки
     * @param source исходная модель поля
     */
    protected void compileConditionsAndDependencies(MenuItem button, AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        String widgetId = initWidgetId(source, context, p);
        List<Condition> enabledConditions = new ArrayList<>();

        if (source.getVisibilityConditions() != null)
            button.getConditions().put(ValidationType.visible,
                    compileConditions(source.getVisibilityConditions(), source.getModel(), widgetId));
        if (source.getEnablingConditions() != null)
            enabledConditions.addAll(compileConditions(source.getEnablingConditions(), source.getModel(), widgetId));

        ComponentScope componentScope = p.getScope(ComponentScope.class);

        Condition emptyModelCondition = enabledByEmptyModelCondition(source, widgetId, componentScope, p);
        if (emptyModelCondition != null)
            enabledConditions.add(emptyModelCondition);

        if (!enabledConditions.isEmpty()) {
            button.getConditions().put(ValidationType.enabled, enabledConditions);
        }

        if (source.getDependencies() != null)
            compileDependencies(source.getDependencies(), button, widgetId, source.getModel(), p);

        if (componentScope != null && componentScope.unwrap(N2oCell.class) != null) {
            button.setVisible(p.resolveJS(source.getVisible()));
            button.setEnabled(p.resolveJS(source.getEnabled()));
        } else {
            if (StringUtils.isLink(source.getVisible()))
                compileLinkCondition(button, widgetId, ValidationType.visible, source.getVisible(), source.getModel());
            else
                button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));

            if (StringUtils.isLink(source.getEnabled()))
                compileLinkCondition(button, widgetId, ValidationType.enabled, source.getEnabled(), source.getModel());
            else
                button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        }
    }

    /**
     * Получение условия доступности кнопки при пустой модели
     *
     * @param source         Абстрактная модель пункта меню
     * @param widgetId       Идентификатор виджета, к которому относится кнопка
     * @param componentScope Родительский компонент
     * @param p              Процессор сборки метаданных
     * @return Условие доступности кнопки при пустой модели
     */
    private Condition enabledByEmptyModelCondition(AbstractMenuItem source, String widgetId, ComponentScope componentScope, CompileProcessor p) {
        DisableOnEmptyModelType disableOnEmptyModel = p.cast(source.getDisableOnEmptyModel(),
                p.resolve(property("n2o.api.button.disable_on_empty_model"), DisableOnEmptyModelType.class));
        if (DisableOnEmptyModelType.FALSE.equals(disableOnEmptyModel)) return null;

        boolean parentIsNotCell = componentScope == null || componentScope.unwrap(N2oCell.class) == null;
        boolean autoDisableCondition = DisableOnEmptyModelType.AUTO.equals(disableOnEmptyModel) &&
                ReduxModel.RESOLVE.equals(source.getModel()) &&
                parentIsNotCell;

        if (DisableOnEmptyModelType.TRUE.equals(disableOnEmptyModel) || autoDisableCondition) {
            Condition condition = new Condition();
            condition.setExpression("!_.isEmpty(this)");
            condition.setModelLink(new ModelLink(source.getModel(), widgetId).getBindLink());
            return condition;
        }
        return null;
    }

    private List<Condition> compileConditions(N2oButtonCondition[] conditions, ReduxModel model, String widgetId) {
        List<Condition> result = new ArrayList<>();
        for (N2oButtonCondition n2oCondition : conditions) {
            Condition condition = new Condition();
            condition.setExpression(n2oCondition.getExpression().trim());
            condition.setModelLink(new ModelLink(model, widgetId).getBindLink());
            result.add(condition);
        }
        return result;
    }

    private void compileLinkCondition(MenuItem button, String widgetId, ValidationType type,
                                      String linkCondition, ReduxModel model) {
        Condition condition = new Condition();
        condition.setExpression(linkCondition.substring(1, linkCondition.length() - 1));
        condition.setModelLink(new ModelLink(model, widgetId).getBindLink());
        if (!button.getConditions().containsKey(type))
            button.getConditions().put(type, new ArrayList<>());
        button.getConditions().get(type).add(condition);
    }

    private void compileDependencies(AbstractMenuItem.Dependency[] dependencies, MenuItem button, String widgetId,
                                     ReduxModel buttonModel, CompileProcessor p) {
        for (AbstractMenuItem.Dependency d : dependencies) {
            ValidationType validationType = null;
            if (d instanceof AbstractMenuItem.EnablingDependency)
                validationType = ValidationType.enabled;
            else if (d instanceof AbstractMenuItem.VisibilityDependency)
                validationType = ValidationType.visible;

            compileCondition(d, button, validationType, widgetId, buttonModel, p);
        }
    }

    private void compileCondition(AbstractMenuItem.Dependency dependency, MenuItem menuItem, ValidationType validationType,
                                  String widgetId, ReduxModel buttonModel, CompileProcessor p) {
        String refWidgetId = null;
        if (dependency.getRefWidgetId() != null) {
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null) {
                refWidgetId = pageScope.getGlobalWidgetId(dependency.getRefWidgetId());
            }
        }
        refWidgetId = p.cast(refWidgetId, widgetId);
        ReduxModel refModel = p.cast(dependency.getRefModel(), buttonModel, ReduxModel.RESOLVE);

        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        condition.setModelLink(new ModelLink(refModel, refWidgetId, null).getBindLink());
        if (dependency instanceof AbstractMenuItem.EnablingDependency)
            condition.setMessage(((AbstractMenuItem.EnablingDependency) dependency).getMessage());

        if (!menuItem.getConditions().containsKey(validationType))
            menuItem.getConditions().put(validationType, new ArrayList<>());
        menuItem.getConditions().get(validationType).add(condition);
    }
}
