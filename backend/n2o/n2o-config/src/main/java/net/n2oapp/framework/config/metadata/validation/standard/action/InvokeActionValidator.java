package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор InvokeAction
 */
@Component
public class InvokeActionValidator implements SourceValidator<N2oInvokeAction>, SourceClassAware {
    @Override
    public void validate(N2oInvokeAction source, SourceProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        if (source.getRefreshWidgetId() != null && pageScope != null
                && pageScope.getWidgetIds() != null && !pageScope.getWidgetIds().contains(source.getRefreshWidgetId())) {
            checkRefreshDatasources(source, datasourceIdsScope);
            throw new N2oMetadataValidationException(p.getMessage(
                    "Атрибут refresh-widget-id ссылается на несуществующий виджет: " + source.getRefreshWidgetId()));
        }

        checkDatasource(source, datasourceIdsScope);
        checkRefreshDatasources(source, datasourceIdsScope);
    }

    /**
     * Проверка существования источника данных, на который ссылается действие вызова операции
     * @param source            Действие вызова операции
     * @param datasourceIdsScope  Скоуп источников данных
     */
    private void checkDatasource(N2oInvokeAction source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getDatasource() != null) {
            String operation = ValidationUtils.getIdOrEmptyString(source.getOperationId());
            ValidationUtils.checkForExistsDatasource(source.getDatasource(), datasourceIdsScope,
                    String.format("Действие %s ссылается на несуществующий источник данных '%s'", operation, source.getDatasource()));
        }
    }

    /**
     * Проверка существования источника данных, который необходимо обновить после успешного выполнения операции
     * @param source            Действие вызова операции
     * @param datasourceIdsScope  Скоуп источников данных
     */
    private void checkRefreshDatasources(N2oInvokeAction source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getRefreshDatasources() != null) {
            for (String refreshDs : source.getRefreshDatasources()) {
                String operation = ValidationUtils.getIdOrEmptyString(source.getOperationId());
                ValidationUtils.checkForExistsDatasource(refreshDs, datasourceIdsScope,
                        String.format("Атрибут \"refresh-datasources\" действия %s ссылается на несуществующий источник данных '%s'",
                                operation, refreshDs));
            }
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInvokeAction.class;
    }
}
