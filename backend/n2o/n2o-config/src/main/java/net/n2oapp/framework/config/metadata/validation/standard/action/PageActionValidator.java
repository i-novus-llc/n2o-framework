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

/**
 * Валидатор действия открытия страницы
 */
@Component
public class PageActionValidator implements SourceValidator<N2oAbstractPageAction>, SourceClassAware {
    @Override
    public void validate(N2oAbstractPageAction source, SourceProcessor p) {
        p.checkForExists(source.getObjectId(), N2oObject.class,
                "Действие открытия страницы: " + source.getId() +
                        " ссылается на несуществующий объект: " + source.getObjectId());

        p.checkForExists(source.getPageId(), N2oPage.class,
                "Действие открытия страницы: " + source.getId() +
                        " ссылается на несуществующую страницу: " + source.getPageId());
        if (source.getSubmitOperationId() != null && source.getObjectId() != null) {
            N2oObject object = p.getOrThrow(source.getObjectId(), N2oObject.class);
            p.safeStreamOf(object.getOperations()).
                    filter(operation -> source.getSubmitOperationId().equals(operation.getId())).
                    findFirst().orElseThrow(() -> new N2oMetadataValidationException("Действие открытия страницы: " + source.getId() +
                    " ссылается на несуществующую в объекте: " + source.getObjectId() + " операцию: " + source.getSubmitOperationId()));
        }
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getRefreshWidgetId() != null && pageScope != null
                && !pageScope.getWidgetIds().contains(source.getRefreshWidgetId())) {
            throw new N2oMetadataValidationException(p.getMessage(
                    "Атрибут refresh-widget-id ссылается на несуществующий виджет: " + source.getRefreshWidgetId()));
        }

        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
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
     * Проверка существования источника данных для копирования при открытии модального окна
     * @param source           Действие открытия страницы
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkTargetDatasource(N2oAbstractPageAction source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getTargetDatasource() != null) {
            String openPage = ValidationUtils.getIdOrEmptyString(source.getPageId());
            ValidationUtils.checkForExistsDatasource(source.getTargetDatasource(), datasourceIdsScope,
                    String.format("Атрибут \"target-datasource\" действия открытия страницы %s ссылается на несущетсвующий источник данных '%s'",
                            openPage, source.getTargetDatasource()));
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }
}
