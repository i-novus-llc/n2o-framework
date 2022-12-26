package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

/**
 * Валидация списковых компонентов
 */
@Component
public class ListFieldValidator implements SourceValidator<N2oListField>, SourceClassAware {

    @Override
    public void validate(N2oListField field, SourceProcessor p) {
        p.checkForExists(field.getQueryId(), N2oQuery.class,
                String.format("Поле '%s' ссылается на несуществующую выборку '%s'", field.getId(), field.getQueryId()));

        if (field.getQueryId() != null && field.getDatasourceId() != null)
            throw new N2oMetadataValidationException(
                    String.format("Поле '%s' использует выборку и ссылку на источник данных одновременно", field.getId()));
        else if (field.getQueryId() != null && field.getOptions() != null)
            throw new N2oMetadataValidationException(
                    String.format("Поле '%s' использует выборку и элемент '<options>' одновременно", field.getId()));
        else if (field.getDatasourceId() != null && field.getOptions() != null)
            throw new N2oMetadataValidationException(
                    String.format("Поле '%s' использует ссылку на источник данных и элемент '<options>' одновременно", field.getId()));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListField.class;
    }
}


