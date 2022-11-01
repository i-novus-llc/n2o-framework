package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор действия открытия страницы
 */
@Component
public class PageActionValidator implements SourceValidator<N2oAbstractPageAction>, SourceClassAware {
    @Override
    public void validate(N2oAbstractPageAction source, SourceProcessor p) {
        p.checkForExists(source.getObjectId(), N2oObject.class,
                "Действие открытия страницы " + getIdOrEmptyString(source.getId()) +
                        " ссылается на несуществующий объект " + source.getObjectId());

        p.checkForExists(source.getPageId(), N2oPage.class,
                "Действие открытия страницы " + getIdOrEmptyString(source.getId()) +
                        " ссылается на несуществующую страницу " + source.getPageId());
        if (source.getSubmitOperationId() != null && source.getObjectId() != null) {
            N2oObject object = p.getOrThrow(source.getObjectId(), N2oObject.class);
            p.safeStreamOf(object.getOperations()).
                    filter(operation -> source.getSubmitOperationId().equals(operation.getId())).
                    findFirst().orElseThrow(() -> new N2oMetadataValidationException("Действие открытия страницы " + getIdOrEmptyString(source.getId()) +
                    " ссылается на несуществующую в объекте " + source.getObjectId() + " операцию " + source.getSubmitOperationId()));
        }
        PageScope pageScope = p.getScope(PageScope.class);
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        checkRefreshWidgetDatasourceIds(source, pageScope, datasourceIdsScope);

        if (source.getDatasources() != null) {
            DatasourceIdsScope actionDatasourceScope = new DatasourceIdsScope(datasourceIdsScope);
            Arrays.stream(source.getDatasources())
                    .filter(datasource -> datasource.getId() != null)
                    .forEach(datasource -> actionDatasourceScope.add(datasource.getId()));
            Arrays.stream(source.getDatasources())
                    .filter(datasource -> datasource.getId() != null)
                    .forEach(datasource -> p.validate(datasource, actionDatasourceScope));
        }
    }

    /**
     * Проверка существования виджета\источника для рефреша при открытии модального окна
     *
     * @param source             Действие открытие страницы
     * @param pageScope          Скоуп страницы
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkRefreshWidgetDatasourceIds(N2oAbstractPageAction source, PageScope pageScope,
                                                 DatasourceIdsScope datasourceIdsScope) {
        if (source.getRefreshDatasourceIds() == null)
            return;
        String[] refreshDatasourceIds = source.getRefreshDatasourceIds();
        boolean isNotDatasource = false;
        if (refreshDatasourceIds.length == 1) {
            if (datasourceIdsScope != null && !datasourceIdsScope.contains(refreshDatasourceIds[0]))
                isNotDatasource = true;
            if (isNotDatasource && pageScope != null && !pageScope.getWidgetIds().contains(source.getRefreshWidgetId()))
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'refresh-datasources'\\'refresh-widget-id' ссылается на несуществующий источник\\виджет '%s'", refreshDatasourceIds[0]));
        } else if (datasourceIdsScope != null) {
            for (String datasourceId : refreshDatasourceIds) {
                ValidationUtils.checkDatasourceExistence(datasourceId, datasourceIdsScope,
                        String.format("Атрибут \"refresh-datasources\" ссылается на несуществующий источник данных '%s'", datasourceId));
            }
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }
}
