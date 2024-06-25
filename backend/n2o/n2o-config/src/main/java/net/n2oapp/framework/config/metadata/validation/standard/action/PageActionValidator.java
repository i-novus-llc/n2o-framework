package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidatorDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.Objects.nonNull;
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
        checkRefreshWidgetDatasourceIds(source, pageScope, p);

        ValidatorDatasourceIdsScope datasourceIdsScope = p.getScope(ValidatorDatasourceIdsScope.class);
        if (source.getDatasources() != null && datasourceIdsScope != null) {
            ValidatorDatasourceIdsScope actionDatasourceScope = new ValidatorDatasourceIdsScope(datasourceIdsScope);
            Arrays.stream(source.getDatasources())
                    .filter(datasource -> datasource.getId() != null)
                    .forEach(datasource -> actionDatasourceScope.add(datasource.getId()));
            Arrays.stream(source.getDatasources())
                    .filter(datasource -> datasource.getId() != null)
                    .forEach(datasource -> p.validate(datasource, actionDatasourceScope));
        }
        checkDatasourceInParam(source, p);
        checkEmptyToolbar(source);
    }

    private static void checkEmptyToolbar(N2oAbstractPageAction source) {
        N2oToolbar[] toolbars = source.getToolbars();
        if (nonNull(toolbars))
            for (N2oToolbar toolbar : toolbars) {
                if (toolbar.getItems() == null && toolbar.getGenerate() == null)
                    throw new N2oMetadataValidationException(
                            String.format("Не заданы элементы или атрибут 'generate' в тулбаре действия открытия страницы %s",
                                    ValidationUtils.getIdOrEmptyString(source.getPageId())));
            }
    }

    /**
     * Проверка существования виджета\источника для рефреша при открытии модального окна
     *
     * @param source    Действие открытие страницы
     * @param pageScope Скоуп страницы
     */
    private void checkRefreshWidgetDatasourceIds(N2oAbstractPageAction source, PageScope pageScope, SourceProcessor p) {
        if (source.getRefreshDatasourceIds() == null)
            return;
        String[] refreshDatasourceIds = source.getRefreshDatasourceIds();
        boolean isNotDatasource = false;
        ValidatorDatasourceIdsScope datasourceIdsScope = p.getScope(ValidatorDatasourceIdsScope.class);
        if (refreshDatasourceIds.length == 1) {
            if (datasourceIdsScope != null && !datasourceIdsScope.contains(refreshDatasourceIds[0]))
                isNotDatasource = true;
            if (isNotDatasource && pageScope != null && !pageScope.getWidgetIds().contains(source.getRefreshWidgetId()))
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'refresh-datasources'\\'refresh-widget-id' ссылается на несуществующий источник\\виджет %s",
                                ValidationUtils.getIdOrEmptyString(refreshDatasourceIds[0])));
        } else if (datasourceIdsScope != null) {
            for (String datasourceId : refreshDatasourceIds) {
                ValidationUtils.checkDatasourceExistence(datasourceId, p,
                        String.format("Атрибут \"refresh-datasources\" ссылается на несуществующий источник данных %s",
                                ValidationUtils.getIdOrEmptyString(datasourceId)));
            }
        }
    }

    private void checkDatasourceInParam(N2oAbstractPageAction action, SourceProcessor p) {
        N2oParam[] params = action.getParams();
        if (params != null)
            Arrays.stream(params).forEach(param -> {
                if (param.getName() == null)
                    throw new N2oMetadataValidationException(String.format("В параметре действия открытия страницы %s не задан атрибут 'name'",
                            ValidationUtils.getIdOrEmptyString(action.getPageId())));

                if (param.getDatasourceId() != null)
                    ValidationUtils.checkDatasourceExistence(param.getDatasourceId(), p,
                            String.format("Параметр %s открытия страницы ссылается на несуществующий источник данных %s",
                                    ValidationUtils.getIdOrEmptyString(param.getName()),
                                    ValidationUtils.getIdOrEmptyString(param.getDatasourceId())));
            });
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }
}
