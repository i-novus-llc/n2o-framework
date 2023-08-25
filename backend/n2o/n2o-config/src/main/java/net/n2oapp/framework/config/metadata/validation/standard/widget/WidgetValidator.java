package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * Валидатор виджета
 */
@Component
public class WidgetValidator implements SourceValidator<N2oWidget>, SourceClassAware {

    @Override
    public void validate(N2oWidget source, SourceProcessor p) {
        MetaActions pageActions = p.getScope(MetaActions.class);
        checkActionIds(source, pageActions, p);
        MetaActions allMetaActions = joinMetaActions(pageActions, source.getActions(), p);

        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        ComponentScope componentScope = new ComponentScope(source);
        if (nonNull(source.getDatasource())) {
            WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasource(), null, (N2oCompileProcessor) p);
            p.validate(source.getDatasource(), widgetScope, datasourceIdsScope);
        }

        if (nonNull(source.getToolbars())) {
            List<N2oButton> menuItems = new ArrayList<>();
            for (N2oToolbar toolbar : source.getToolbars()) {
                if (nonNull(toolbar.getItems())) {
                    for (ToolbarItem item : toolbar.getItems()) {
                        if (item instanceof N2oButton) {
                            menuItems.add((N2oButton) item);
                        } else if (item instanceof N2oSubmenu && (((N2oSubmenu) item).getMenuItems() != null)) {
                            menuItems.addAll(Arrays.asList(((N2oSubmenu) item).getMenuItems()));
                        }
                    }
                }
            }
            p.safeStreamOf(menuItems).forEach(menuItem -> p.validate(menuItem, datasourceIdsScope, componentScope,
                    allMetaActions));
            p.checkIdsUnique(menuItems, String.format("Кнопка '%s' встречается более чем один раз в виджете '%s'", "%s", source.getId()));
        }

        if (nonNull(source.getDatasourceId())) {
            checkDatasource(source, datasourceIdsScope);
        }

        checkDependencies(source);
        p.safeStreamOf(source.getActions()).flatMap(actionBar -> p.safeStreamOf(actionBar.getN2oActions()))
                .forEach(action -> p.validate(action, componentScope));

        checkEmptyToolbar(source);
    }

    private static void checkEmptyToolbar(N2oWidget source) {
        if (nonNull(source.getToolbars()))
            for (N2oToolbar toolbar : source.getToolbars()) {
                if (toolbar.getItems() == null && toolbar.getGenerate() == null)
                    throw new N2oMetadataValidationException(
                            String.format("Не заданы элементы или атрибут 'generate' в тулбаре виджета '%s'",
                                    source.getId()));
            }
    }

    private void checkDependencies(N2oWidget source) {
        if (source.getDependencies() != null) {
            Arrays.stream(source.getDependencies()).forEach(dependency ->
                    ValidationUtils.checkEmptyDependency(dependency,
                            String.format("Зависимость виджета %s имеет пустое тело",
                                    getIdOrEmptyString(source.getId()))));
        }
    }

    private MetaActions joinMetaActions(MetaActions pageActions, ActionBar[] widgetActions, SourceProcessor p) {
        MetaActions result = new MetaActions();
        if (nonNull(pageActions))
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
    private void checkActionIds(N2oWidget source, MetaActions pageActions, SourceProcessor p) {
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

        if (isNull(pageActions))
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
     *
     * @param n2oWidget Виджет
     * @param scope     Скоуп источников данных
     */
    private void checkDatasource(N2oWidget n2oWidget, DatasourceIdsScope scope) {
        if (n2oWidget.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(n2oWidget.getDatasourceId(), scope,
                String.format("Виджет %s cсылается на несуществующий источник данных '%s'",
                        ValidationUtils.getIdOrEmptyString(n2oWidget.getId()), n2oWidget.getDatasourceId()));
        if (nonNull(n2oWidget.getDatasource()) && nonNull(n2oWidget.getDatasourceId()))
            throw new N2oMetadataValidationException(
                    String.format("Виджет '%s' использует внутренний источник и ссылку на источник данных одновременно",
                            n2oWidget.getId())
            );
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }
}
