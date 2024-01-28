package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.action.N2oAbstractMetaAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

/**
 * Абстрактная реализация валидатора действия, содержащего стандартные саги
 */
public abstract class AbstractMetaActionValidator<S extends N2oAbstractMetaAction> implements SourceValidator<S>, SourceClassAware {
    @Override
    public void validate(S source, SourceProcessor p) {
        if (source.getRefreshDatasourceIds() != null) {
            checkRefreshDatasources(source, p);
        }
        checkRefreshDatasources(source, p);
    }

    /**
     * Проверка существования источника данных, который необходимо обновить после успешного выполнения действия
     *
     * @param source             Действие вызова операции
     */
    private void checkRefreshDatasources(S source, SourceProcessor p) {
        if (source.getRefreshDatasourceIds() != null)
            for (String refreshDs : source.getRefreshDatasourceIds()) {
                String operation = ValidationUtils.getIdOrEmptyString(source.getOperationId());
                ValidationUtils.checkDatasourceExistence(refreshDs, p,
                        String.format("Атрибут \"refresh-datasources\" действия %s ссылается на несуществующий источник данных '%s'",
                                operation, refreshDs));
            }
    }
}
