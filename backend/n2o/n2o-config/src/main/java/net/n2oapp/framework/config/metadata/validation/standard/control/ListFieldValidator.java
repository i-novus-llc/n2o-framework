package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkQueryExists;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидация списковых компонентов
 */
@Component
public class ListFieldValidator implements SourceValidator<N2oListField>, SourceClassAware {

    @Override
    public void validate(N2oListField field, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        checkQueryExists(field.getQueryId(),
                String.format("Поле %s", getIdOrEmptyString(field.getId())), p);
        if (field.getQueryId() != null && field.getDatasourceId() != null)
            throw new N2oMetadataValidationException(
                    String.format("Поле '%s' использует выборку и ссылку на источник данных одновременно", field.getId()));
        else if (field.getQueryId() != null && field.getOptions() != null)
            throw new N2oMetadataValidationException(
                    String.format("Поле '%s' использует выборку и элемент '<options>' одновременно", field.getId()));
        else if (field.getDatasourceId() != null && field.getOptions() != null)
            throw new N2oMetadataValidationException(
                    String.format("Поле '%s' использует ссылку на источник данных и элемент '<options>' одновременно", field.getId()));

        if (field.getDatasourceId() != null) {
            checkDatasource(field, widgetScope, p);
        }
    }

    private void checkDatasource(N2oListField source, WidgetScope widgetScope, SourceProcessor p) {
        ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                String.format("Указан несуществующий источник данных %s для поля %s виджета %s",
                        ValidationUtils.getIdOrEmptyString(source.getDatasourceId()),
                        ValidationUtils.getIdOrEmptyString(source.getId()),
                        ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListField.class;
    }
}


