package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oClipboardButton;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.DatasourceUtil;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.initDatasource;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Базовая компиляция кнопки
 */
public abstract class BaseButtonCompiler<S extends N2oAbstractButton, B extends AbstractButton> implements BaseSourceCompiler<B, S, CompileContext<?, ?>> {
    private static final String PROPERTY_PREFIX = "n2o.api.control.button";

    protected void compileBase(AbstractButton button, N2oAbstractButton source, CompileProcessor p) {
        button.setId(source.getId());
        button.setProperties(p.mapAttributes(source));
        button.setIcon(p.resolveJS(source.getIcon()));
        button.setIconPosition(castDefault(source.getIconPosition(),
                () -> p.resolve(property("n2o.api.button.icon_position"), PositionEnum.class)));
        button.setLabel(p.resolveJS(source.getLabel()));
        button.setDatasource(DatasourceUtil.getClientDatasourceId(source.getDatasourceId(), p));
        button.setClassName(source.getCssClass());
        button.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        button.setColor(p.resolveJS(source.getColor()));
        button.setModel(source.getModel());
        button.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));
        initHint(source, button, p);
    }

    /**
     * Задание подсказки кнопки
     *
     * @param source Исходная модель кнопки
     * @param button Клиентская модель кнопки
     * @param p      Процессор сборки метаданных
     */
    private void initHint(N2oAbstractButton source, AbstractButton button, CompileProcessor p) {
        String hint;
        hint = source.getDescription();

        if (hint != null) {
            button.setHint(p.resolveJS(hint.trim()));
            button.setHintPosition(source.getTooltipPosition());
        }
    }

    /**
     * Задание цвета кнопки
     *
     * @param source Исходная модель кнопки
     * @param p      Процессор сборки метаданных
     */
    private String initColor(N2oAbstractButton source, CompileProcessor p) {
        if (source.getColor() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                N2oCell cell = componentScope.unwrap(N2oCell.class);
                if (cell != null) {
                    return p.resolve(property("n2o.api.cell.toolbar.button-color"), String.class);
                }
            }
        }
        return source.getColor();
    }

    protected void initDefaults(S source, CompileProcessor p) {
        PageIndexScope pageIndexScope = p.getScope(PageIndexScope.class);
        String defaultId = ("_".equals(pageIndexScope.getPageId()) ? "mi" : pageIndexScope.getPageId() + "_mi") + pageIndexScope.get();
        source.setId(castDefault(source.getId(), defaultId));

        if (Boolean.TRUE.equals(p.resolve(property("n2o.api.button.generate-label"), Boolean.class)))
            source.setLabel(castDefault(source.getLabel(), source.getId()));
        source.setTooltipPosition(initTooltipPosition(source, p));
        source.setColor(initColor(source, p));

        source.setDatasourceId(initDatasource(source, p));
        source.setModel(castDefault(source.getModel(), ReduxModelEnum.RESOLVE));
    }

    private String initTooltipPosition(S source, CompileProcessor p) {
        if (source.getTooltipPosition() != null)
            return source.getTooltipPosition();
        return (source instanceof N2oButton)
                ? p.resolve(property("n2o.api.button.tooltip_position"), String.class)
                : p.resolve(property("n2o.api.menuitem.tooltip_position"), String.class);
    }

    protected void compileCondition(N2oAbstractButton source, AbstractButton button, CompileProcessor p, ComponentScope componentScope) {
        if (componentScope != null && componentScope.unwrap(N2oCell.class) != null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            String widgetDs = nonNull(widgetScope) && nonNull(widgetScope.getDatasourceId()) ? widgetScope.getDatasourceId() : null;
            if (widgetDs != null && !widgetDs.equals(source.getDatasourceId()))
                compileLinkConditions(source, button, p);
            else
                resolveConditions(source, button, p);
        } else {
            compileLinkConditions(source, button, p);
        }
    }

    private void compileLinkConditions(N2oAbstractButton source, AbstractButton button, CompileProcessor p) {
        String clientDatasource = getClientDatasourceId(source.getDatasourceId(), p);
        if (isLink(source.getVisible()))
            compileLink(button, clientDatasource, ValidationTypeEnum.VISIBLE, source.getVisible(), source.getModel());
        else
            button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));

        if (isLink(source.getEnabled()))
            compileLink(button, clientDatasource, ValidationTypeEnum.ENABLED, source.getEnabled(), source.getModel());
        else
            button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
    }

    private void resolveConditions(N2oAbstractButton source, AbstractButton button, CompileProcessor p) {
        button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
    }

    private void compileLink(AbstractButton button, String clientDatasource, ValidationTypeEnum type,
                             String linkCondition, ReduxModelEnum model) {
        Condition condition = new Condition();
        condition.setExpression(unwrapLink(linkCondition));
        condition.setModelLink(new ModelLink(model, clientDatasource).getLink());
        if (!button.getConditions().containsKey(type))
            button.getConditions().put(type, new ArrayList<>());
        button.getConditions().get(type).add(condition);
    }

    /**
     * Компиляция условий и зависимостей кнопки
     *
     * @param button Клиентская модель кнопки
     * @param source Исходная модель кнопки
     * @param p      Процессор сборки метаданных
     */
    protected void compileDependencies(N2oAbstractButton source, AbstractButton button,
                                       CompileProcessor p) {
        String clientDatasource = getClientDatasourceId(source.getDatasourceId(), p);

        ComponentScope componentScope = p.getScope(ComponentScope.class);

        List<Condition> enabledConditions = new ArrayList<>();
        if (source.getDatasourceId() != null) {
            Condition emptyModelCondition = enabledByEmptyModelCondition(source, clientDatasource, componentScope, p);
            if (emptyModelCondition != null)
                enabledConditions.add(emptyModelCondition);
        }

        if (!enabledConditions.isEmpty()) {
            button.getConditions().put(ValidationTypeEnum.ENABLED, enabledConditions);
        }
        if (source instanceof N2oButton n2oButton)
            compileDependencies(n2oButton.getDependencies(), button, clientDatasource, source.getModel(), p);
        if (source instanceof N2oClipboardButton n2oClipboardButton)
            compileDependencies(n2oClipboardButton.getDependencies(), button, clientDatasource, source.getModel(), p);

        compileCondition(source, button, p, componentScope);
    }

    /**
     * Получение условия доступности кнопки при пустой модели
     *
     * @param source           Исходная модель кнопки
     * @param clientDatasource Идентификатор источника данных, к которому относится кнопка
     * @param componentScope   Родительский компонент
     * @param p                Процессор сборки метаданных
     * @return Условие доступности кнопки при пустой модели
     */
    private Condition enabledByEmptyModelCondition(N2oAbstractButton source, String clientDatasource, ComponentScope componentScope, CompileProcessor p) {
        DisableOnEmptyModelTypeEnum disableOnEmptyModel;

        if (source instanceof N2oButton n2oButton)
            disableOnEmptyModel = castDefault(n2oButton.getDisableOnEmptyModel(),
                    () -> p.resolve(property("n2o.api.button.disable_on_empty_model"), DisableOnEmptyModelTypeEnum.class));

        else if (source instanceof N2oClipboardButton n2oClipboardButton)
            disableOnEmptyModel = castDefault(n2oClipboardButton.getDisableOnEmptyModel(),
                    () -> p.resolve(property("n2o.api.button.disable_on_empty_model"), DisableOnEmptyModelTypeEnum.class));
        else return null;

        if (DisableOnEmptyModelTypeEnum.FALSE.equals(disableOnEmptyModel)) return null;

        boolean parentIsNotCell = componentScope == null || componentScope.unwrap(N2oCell.class) == null;
        boolean autoDisableCondition = DisableOnEmptyModelTypeEnum.AUTO.equals(disableOnEmptyModel) &&
                (ReduxModelEnum.RESOLVE.equals(source.getModel()) || ReduxModelEnum.MULTI.equals(source.getModel())) &&
                parentIsNotCell;

        if (DisableOnEmptyModelTypeEnum.TRUE.equals(disableOnEmptyModel) || autoDisableCondition) {
            Condition condition = new Condition();
            condition.setExpression("!$.isEmptyModel(this)");
            condition.setModelLink(new ModelLink(source.getModel(), clientDatasource).getLink());

            return condition;
        }

        return null;
    }

    private void compileDependencies(N2oAbstractButton.Dependency[] dependencies, AbstractButton button, String clientDatasource,
                                     ReduxModelEnum buttonModel, CompileProcessor p) {
        if (dependencies == null) return;
        for (N2oAbstractButton.Dependency d : dependencies) {
            ValidationTypeEnum validationType = null;
            if (d instanceof N2oAbstractButton.EnablingDependency)
                validationType = ValidationTypeEnum.ENABLED;
            else if (d instanceof N2oAbstractButton.VisibilityDependency)
                validationType = ValidationTypeEnum.VISIBLE;

            compileDependencyCondition(d, button, validationType, clientDatasource, buttonModel, p);
        }
    }

    private void compileDependencyCondition(N2oAbstractButton.Dependency dependency, AbstractButton button, ValidationTypeEnum validationType,
                                            String buttonDatasource, ReduxModelEnum buttonModel, CompileProcessor p) {
        ReduxModelEnum refModel = castDefault(dependency.getModel(), buttonModel, ReduxModelEnum.RESOLVE);
        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        String datasource = (dependency.getDatasource() != null) ?
                getClientDatasourceId(dependency.getDatasource(), p) :
                buttonDatasource;
        condition.setModelLink(new ModelLink(refModel, datasource, null).getLink());
        if (dependency instanceof N2oAbstractButton.EnablingDependency enablingDependency)
            condition.setMessage(enablingDependency.getMessage());

        if (!button.getConditions().containsKey(validationType))
            button.getConditions().put(validationType, new ArrayList<>());
        button.getConditions().get(validationType).add(condition);
    }
}
