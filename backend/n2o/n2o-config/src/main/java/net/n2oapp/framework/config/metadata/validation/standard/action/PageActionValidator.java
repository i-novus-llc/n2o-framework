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
        checkRefreshWidgetId(source, pageScope, datasourceIdsScope, p);
        checkRefreshDatasourceIds(source, datasourceIdsScope, pageScope);
        checkTargetDatasource(source, datasourceIdsScope);

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
     *
     * Проверка существования источников данных для рефреша при открытии модального окна
     *
     * @param source Дествие открытия страницы
     * @param datasourceIdsScope Скоуп источников данных
     * @param pageScope Скоуп страницы
     */
    private void checkRefreshDatasourceIds(N2oAbstractPageAction source, DatasourceIdsScope datasourceIdsScope, PageScope pageScope) {
        String[] refreshDatasourceIds = source.getRefreshDatasourceIds();
        if (refreshDatasourceIds != null && datasourceIdsScope != null && !isWidgetInRefreshDatasources(source, pageScope)) {
            for (String datasourceId : refreshDatasourceIds) {
                ValidationUtils.checkForExistsDatasource(datasourceId, datasourceIdsScope,
                        String.format("Атрибут \"refresh-datasources\" содержит в себе не существующий источник данных %s", datasourceId));
            }
        }
    }

    /**
     *
     * Проверка существования виджета для рефреша при открытии модального окна
     *
     * @param source Действие открытие страницы
     * @param pageScope Скоуп страницы
     * @param datasourceIdsScope Скоуп источников данных
     * @param p Процессор исходных данных
     */
    private void checkRefreshWidgetId(N2oAbstractPageAction source, PageScope pageScope,
                                      DatasourceIdsScope datasourceIdsScope, SourceProcessor p) {
        String[] refreshDatasourceIds = source.getRefreshDatasourceIds();
        if (refreshDatasourceIds != null && refreshDatasourceIds.length == 1 &&
                !((pageScope != null && pageScope.getWidgetIds().contains(source.getRefreshWidgetId())) ||
                        (datasourceIdsScope != null && datasourceIdsScope.contains(source.getRefreshWidgetId())))) {
            throw new N2oMetadataValidationException(p.getMessage(
                    "Атрибут refresh-widget-id ссылается на несуществующий виджет " + source.getRefreshWidgetId()));
        }
    }

    /**
     * Проверка существования источника данных для копирования при открытии модального окна
     *
     * @param source           Действие открытия страницы
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkTargetDatasource(N2oAbstractPageAction source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getTargetDatasourceId() != null) {
            String openPage = getIdOrEmptyString(source.getPageId());
            ValidationUtils.checkForExistsDatasource(source.getTargetDatasourceId(), datasourceIdsScope,
                    String.format("Атрибут \"target-datasource\" действия открытия страницы %s ссылается на несуществующий источник данных '%s'",
                            openPage, source.getTargetDatasourceId()));
        }
    }

    /**
     *  Проверка, что в refreshDatasource хранится виджет
     *
     * @param source Действие открытия страницы
     * @param pageScope Скоуп страницы
     * @return true - если это виджет, false - если нет
     */
    private boolean isWidgetInRefreshDatasources(N2oAbstractPageAction source, PageScope pageScope) {
        String[] refreshDatasourceIds = source.getRefreshDatasourceIds();
        if (refreshDatasourceIds != null && refreshDatasourceIds.length == 1 && pageScope != null
                && pageScope.getWidgetIds().contains(source.getRefreshWidgetId()))
            return true;
        return false;
    }


    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }
}
