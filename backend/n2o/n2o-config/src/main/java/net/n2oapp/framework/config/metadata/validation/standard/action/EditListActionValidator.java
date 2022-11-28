package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oEditListAction;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор действия редактирования записи списка
 */
@Component
public class EditListActionValidator extends TypedMetadataValidator<N2oEditListAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oEditListAction.class;
    }

    @Override
    public void validate(N2oEditListAction source, SourceProcessor p) {
        if (source.getOperation() == null)
            throw new N2oMetadataValidationException("Для действия <edit-list> не указан тип операции");
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);

        if (source.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), datasourceIdsScope,
                String.format("Действие <edit-list> ссылается на несуществующий источник данных '%s' в атрибуте 'datasource'",
                        source.getDatasourceId()));

        if (source.getItemDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getItemDatasourceId(), datasourceIdsScope,
                    String.format("Действие <edit-list> ссылается на несуществующий источник данных '%s' в атрибуте 'item-datasource'",
                            source.getItemDatasourceId()));
    }
}
