package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Валидатор виджета
 */
@Component
public class WidgetValidator implements SourceValidator<N2oWidget>, SourceClassAware {

    @Override
    public void validate(N2oWidget n2oWidget, SourceProcessor p) {
        checkObjectExists(n2oWidget, p);

        if (n2oWidget.getToolbars() != null) {
            List<N2oButton> menuItems = new ArrayList<>();
            for (N2oToolbar toolbar : n2oWidget.getToolbars()) {
                if (toolbar.getItems() != null) {
                    for (ToolbarItem item : toolbar.getItems()) {
                        if (item instanceof N2oButton) {
                            menuItems.add((N2oButton) item);
                        } else if (item instanceof N2oSubmenu) {
                            menuItems.addAll(Arrays.asList(((N2oSubmenu) item).getMenuItems()));
                        }
                    }
                }
            }
            p.safeStreamOf(menuItems).forEach(menuItem -> p.validate(menuItem.getAction()));
            p.checkIdsUnique(menuItems, "Кнопка '{0}' встречается более чем один раз в виджете '" + n2oWidget.getId() + "'!");
        }

        if (n2oWidget.getDatasourceId() != null) {
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            checkDatasource(n2oWidget, dataSourcesScope);
        }
        p.safeStreamOf(n2oWidget.getActions()).forEach(actionsBar -> p.validate(actionsBar.getAction()));
    }

    private void checkDatasource(N2oWidget n2oWidget, DataSourcesScope scope) {
        ValidationUtils.checkForExistsDatasource(n2oWidget.getDatasourceId(), scope,
                String.format("Виджет '%s' сылается на несуществующий источник данных '%s'", n2oWidget.getId(), n2oWidget.getDatasourceId()));
    }

    /**
     * Проверка валидации префильтров виджета
     *
     * @param n2oWidget Виджет
     * @param query     Выборка виджета
     */
//    @Deprecated
//    private void checkPrefiltersValidation(N2oWidget n2oWidget, N2oQuery query) {
//        if (n2oWidget.getPreFilters() != null) {
//            if (query == null)
//                throw new N2oMetadataValidationException(
//                        String.format("Виджет '%s' имеет префильтры, но не задана выборка", n2oWidget.getId()));
//            if (query.getFields() == null)
//                throw new N2oMetadataValidationException(
//                        String.format("Виджет '%s' имеет префильтры, но в выборке '%s' нет fields!", n2oWidget.getId(), query.getId()));
//
//            for (N2oPreFilter preFilter : n2oWidget.getPreFilters()) {
//                if (preFilter.getValue() != null && preFilter.getParam() != null && (!Boolean.TRUE.equals(preFilter.getRoutable()))) {
//                    throw new N2oMetadataValidationException(
//                            String.format("В префильтре по полю '%s' указан value и param, но при этом routable=false, что противоречит логике работы префильтров!",
//                                    preFilter.getFieldId() == null ? "" : preFilter.getFieldId()));
//                }
//                N2oQuery.Field exField = null;
//                for (N2oQuery.Field field : query.getFields()) {
//                    if (preFilter.getFieldId().equals(field.getId())) {
//                        exField = field;
//                        break;
//                    }
//                }
//                if (exField == null)
//                    throw new N2oMetadataValidationException(
//                            String.format("В выборке '%s' нет field '%s'!", query.getId() == null ? "" : query.getId(), preFilter.getFieldId()));
//
//                if (exField.getFilterList() == null)
//                    throw new N2oMetadataValidationException(
//                            String.format("В выборке '%s' field '%s' не содержит фильтров!", query.getId() == null ? "" : query.getId(), preFilter.getFieldId()));
//
//                N2oQuery.Filter exFilter = null;
//                for (N2oQuery.Filter filter : exField.getFilterList()) {
//                    if (preFilter.getType() == filter.getType()) {
//                        exFilter = filter;
//                        break;
//                    }
//                }
//                if (exFilter == null)
//                    throw new N2oMetadataValidationException(
//                            String.format("В выборке '%s' field '%s' не содержит фильтр типа '%s'!",
//                                    query.getId() == null ? "" : query.getId(),
//                                    preFilter.getFieldId(),
//                                    preFilter.getType()));
//
//                if (n2oWidget.getDependsOn() == null && n2oWidget.getDetailFieldId() == null &&
//                        preFilter.getRefWidgetId() == null && StringUtils.hasLink(preFilter.getValue())) {
//                    throw new N2oMetadataValidationException(
//                            String.format("В виджете '%s' значение префильтра '%s' является ссылкой, но зависимость для нее не прописана!",
//                                    n2oWidget.getId() == null ? "" : n2oWidget.getId(),
//                                    preFilter.getFieldId()));
//                }
//            }
//        }
//    }

    /**
     * Проверка существования выборки, используемой виджетом, и возврат идентификатора выборки
     *
     * @param n2oWidget Виджет
     * @param p         Процессор исходных метаданных
     * @return Идентификатор выборки или null, если виджет не использует выборку
     */
    private N2oQuery checkQueryExists(N2oWidget n2oWidget, SourceProcessor p) {
        if (n2oWidget.getQueryId() != null) {
            p.checkForExists(n2oWidget.getQueryId(), N2oQuery.class,
                    String.format("Виджет '%s' ссылается на несуществующую выборку '%s'", n2oWidget.getId(), n2oWidget.getQueryId()));
            return p.getOrThrow(n2oWidget.getQueryId(), N2oQuery.class);
        } else if (n2oWidget.getDefaultValuesQueryId() != null) {
            p.checkForExists(n2oWidget.getDefaultValuesQueryId(), N2oQuery.class,
                    String.format("Виджет '%s' ссылается на несуществующую выборку '%s'", n2oWidget.getId(), n2oWidget.getDefaultValuesQueryId()));
            return p.getOrThrow(n2oWidget.getDefaultValuesQueryId(), N2oQuery.class);
        }
        return null;
    }

    /**
     * Проверка существования объекта, используемого виджетом
     *
     * @param n2oWidget Виджет
     * @param p         Процессор исходных метаданных
     */
    private void checkObjectExists(N2oWidget n2oWidget, SourceProcessor p) {
        if (n2oWidget.getObjectId() != null) {
            p.checkForExists(n2oWidget.getObjectId(), N2oObject.class,
                    String.format("Виджет '%s' ссылается на несуществующий объект '%s'", n2oWidget.getId(), n2oWidget.getObjectId()));
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }
}
