package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractMetaAction;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

/**
 * Абстрактная реализация валидатора действия, содержащего стандартные саги
 */
public abstract class AbstractMetaActionValidator<S extends N2oAbstractMetaAction> implements SourceValidator<S>, SourceClassAware {
    @Override
    public void validate(S source, SourceProcessor p) {
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        if (source.getRefreshDatasourceIds() != null) {
            checkRefreshDatasources(source, datasourceIdsScope);
        }
        checkRefreshDatasources(source, datasourceIdsScope);
    }

    /**
     * Проверка существования источника данных, который необходимо обновить после успешного выполнения действия
     *
     * @param source             Действие вызова операции
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkRefreshDatasources(S source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getRefreshDatasourceIds() != null)
            for (String refreshDs : source.getRefreshDatasourceIds()) {
                String operation = ValidationUtils.getIdOrEmptyString(source.getOperationId());
                ValidationUtils.checkForExistsDatasource(refreshDs, datasourceIdsScope,
                        String.format("Атрибут \"refresh-datasources\" действия %s ссылается на несуществующий источник данных '%s'",
                                operation, refreshDs));
            }
    }
}
