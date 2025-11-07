package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.ValidatorDataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.*;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * Валидатор виджета
 */
public abstract class WidgetValidator<T extends N2oWidget> implements SourceValidator<T>, SourceClassAware {

    @Override
    public void validate(T source, SourceProcessor p) {
        MetaActions pageActions = p.getScope(MetaActions.class);
        checkActionIds(source, pageActions, p);
        MetaActions allMetaActions = getAllMetaActions(pageActions, source.getActions(), p);
        if (source.getActions() != null)
            Stream.of(source.getActions()).forEach(actionbar -> {
                checkOnFailAction(actionbar.getN2oActions());
                checkCloseInMultiAction(actionbar.getN2oActions());
            });

        ComponentScope componentScope = new ComponentScope(source);
        if (source.getDatasource() != null) {
            WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasource(), null, (N2oCompileProcessor) p, allMetaActions);
            p.validate(source.getDatasource(), widgetScope);

            ValidatorDataSourcesScope datasourcesScope = p.getScope(ValidatorDataSourcesScope.class);
            if (datasourcesScope != null && !datasourcesScope.containsKey(source.getDatasource().getId()))
                datasourcesScope.put(source.getDatasource().getId(), source.getDatasource());
        }

        if (source.getToolbars() != null) {
            List<N2oButton> menuItems = new ArrayList<>();
            Arrays.stream(source.getToolbars())
                    .filter(toolbar -> toolbar.getItems() != null)
                    .flatMap(toolbar -> Arrays.stream(toolbar.getItems()))
                    .forEach(toolbarItem -> addMenuItems(menuItems, toolbarItem));
            p.safeStreamOf(menuItems).forEach(menuItem ->
                    p.validate(menuItem, componentScope, allMetaActions));
            p.checkIdsUnique(menuItems,
                    String.format("Кнопка '%s' встречается более чем один раз в виджете '%s'", "%s", source.getId()));
        }

        if (source.getDatasourceId() != null) {
            checkDatasource(source, p);
        }

        checkDependencies(source);
        p.safeStreamOf(source.getActions()).flatMap(actionBar -> p.safeStreamOf(actionBar.getN2oActions()))
                .forEach(action -> p.validate(action, componentScope));

        checkEmptyToolbar(source);
    }

    private void addMenuItems(List<N2oButton> menuItems, ToolbarItem toolbarItem) {
        if (toolbarItem instanceof N2oButton n2oButton) {
            menuItems.add(n2oButton);
        } else if (toolbarItem instanceof N2oSubmenu submenu && submenu.getMenuItems() != null) {
            menuItems.addAll(Arrays.asList(submenu.getMenuItems()));
        } else if (toolbarItem instanceof N2oGroup group && group.getItems() != null)
            Arrays.stream(group.getItems()).forEach(groupItem -> addMenuItems(menuItems, groupItem));
    }

    private void checkEmptyToolbar(T source) {
        if (source.getToolbars() != null)
            for (N2oToolbar toolbar : source.getToolbars()) {
                if (toolbar.getItems() == null && toolbar.getGenerate() == null)
                    throw new N2oMetadataValidationException(
                            String.format("Не заданы элементы или атрибут 'generate' в тулбаре виджета '%s'",
                                    source.getId()));
            }
    }

    private void checkDependencies(T source) {
        if (source.getDependencies() != null) {
            Arrays.stream(source.getDependencies()).forEach(dependency ->
                    ValidationUtils.checkEmptyDependency(dependency,
                            String.format("Зависимость виджета %s имеет пустое тело",
                                    getIdOrEmptyString(source.getId()))));
        }
    }

    protected MetaActions getAllMetaActions(MetaActions pageActions, ActionBar[] widgetActions, SourceProcessor p) {
        MetaActions result = new MetaActions();
        if (pageActions != null)
            result.putAll(pageActions);
        result.putAll(p.safeStreamOf(widgetActions)
                .collect(Collectors.toMap(ActionBar::getId, Function.identity())));
        return result;
    }

    /**
     * Проверка идентификаторов действий виджета
     *
     * @param source      Виджет
     * @param pageActions Скоуп действий страницы
     * @param p           Процессор исходных метаданных
     */
    private void checkActionIds(T source, MetaActions pageActions, SourceProcessor p) {
        if (isEmpty(source.getActions()))
            return;
        ActionBar[] widgetActions = source.getActions();

        Arrays.stream(widgetActions).forEach(action -> {
            if (action.getId() == null) {
                throw new N2oMetadataValidationException(String.format("Не задан 'id' у <action> виджета '%s'", source.getId()));
            }
        });

        p.checkIdsUnique(widgetActions,
                String.format("Действие '%s' встречается более чем один раз в метаданной виджета '%s'", "%s", source.getId()));

        if (pageActions == null)
            return;
        for (ActionBar widgetAction : widgetActions) {
            if (pageActions.containsKey(widgetAction.getId()))
                throw new N2oMetadataValidationException(
                        String.format("Идентификатор действия '%s' дублируется на странице и в виджете '%s'",
                                widgetAction.getId(), source.getId())
                );
        }
    }

    /**
     * Проверка существования источника данных, на который ссылается виджет
     */
    private void checkDatasource(T widget, SourceProcessor p) {
        if (widget.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(widget.getDatasourceId(), p,
                    String.format("Виджет %s ссылается на несуществующий источник данных '%s'",
                            ValidationUtils.getIdOrEmptyString(widget.getId()), widget.getDatasourceId()));
        if (widget.getDatasource() != null && widget.getDatasourceId() != null)
            throw new N2oMetadataValidationException(
                    String.format("Виджет '%s' использует внутренний источник и ссылку на источник данных одновременно",
                            widget.getId())
            );
    }
}