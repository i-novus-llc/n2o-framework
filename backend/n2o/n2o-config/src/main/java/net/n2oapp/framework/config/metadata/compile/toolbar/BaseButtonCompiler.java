package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButtonCondition;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ToolbarItem
 */
public abstract class BaseButtonCompiler<S extends N2oAbstractButton, B extends AbstractButton> implements BaseSourceCompiler<B, S, CompileContext<?, ?>> {

    protected void initItem(AbstractButton button, N2oAbstractButton source, IndexScope idx,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setId(source.getId());
        button.setProperties(p.mapAttributes(source));
        if (source.getType() != null && source.getType() == LabelType.icon) {
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType() == LabelType.text) {
            button.setLabel(source.getLabel());
        } else {
            button.setIcon(source.getIcon());
            button.setLabel(source.getLabel());
        }

        WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);
        if (source.getWidgetId() == null && widgetObjectScope != null && widgetObjectScope.size() == 1)
            source.setWidgetId(widgetObjectScope.keySet().iterator().next());

        button.setClassName(source.getCssClass());
        button.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        initColor(source, p, button);

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
    }

    private void initColor(N2oAbstractButton source, CompileProcessor p, AbstractButton button) {
        if (source.getColor() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                N2oCell cell = componentScope.unwrap(N2oCell.class);
                if (cell != null) {
                    button.setColor(p.resolve(property("n2o.api.cell.toolbar.button-color"), String.class));
                }
            }
        } else {
            button.setColor(source.getColor());
        }
    }

    protected String initWidgetId(N2oAbstractButton source, CompileContext<?, ?> context, CompileProcessor p) {
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
    protected void compileConditionsAndDependencies(AbstractButton button, N2oAbstractButton source, CompileContext<?, ?> context, CompileProcessor p) {
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
            compileDependencies(source.getDependencies(), button, widgetId, p);

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
    private Condition enabledByEmptyModelCondition(N2oAbstractButton source, String widgetId, ComponentScope componentScope, CompileProcessor p) {
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

    private void compileLinkCondition(AbstractButton button, String widgetId, ValidationType type,
                                      String linkCondition, ReduxModel model) {
        Condition condition = new Condition();
        condition.setExpression(linkCondition.substring(1, linkCondition.length() - 1));
        condition.setModelLink(new ModelLink(model, widgetId).getBindLink());
        if (!button.getConditions().containsKey(type))
            button.getConditions().put(type, new ArrayList<>());
        button.getConditions().get(type).add(condition);
    }

    private void compileDependencies(N2oAbstractButton.Dependency[] dependencies, AbstractButton button, String widgetId, CompileProcessor p) {
        for (N2oAbstractButton.Dependency d : dependencies) {
            ValidationType validationType = null;
            if (d instanceof N2oAbstractButton.EnablingDependency)
                validationType = ValidationType.enabled;
            else if (d instanceof N2oAbstractButton.VisibilityDependency)
                validationType = ValidationType.visible;

            compileCondition(d, button, validationType, widgetId, p);
        }
    }

    private void compileCondition(N2oAbstractButton.Dependency dependency, AbstractButton button, ValidationType validationType,
                                  String widgetId, CompileProcessor p) {
        String refWidgetId = null;
        if (dependency.getRefWidgetId() != null) {
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null) {
                refWidgetId = pageScope.getGlobalWidgetId(dependency.getRefWidgetId());
            }
        }
        refWidgetId = p.cast(refWidgetId, widgetId);
        ReduxModel refModel = p.cast(dependency.getRefModel(), ReduxModel.RESOLVE);

        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        condition.setModelLink(new ModelLink(refModel, refWidgetId, null).getBindLink());
        if (dependency instanceof N2oAbstractButton.EnablingDependency)
            condition.setMessage(((N2oAbstractButton.EnablingDependency) dependency).getMessage());

        if (!button.getConditions().containsKey(validationType))
            button.getConditions().put(validationType, new ArrayList<>());
        button.getConditions().get(validationType).add(condition);
    }
}
