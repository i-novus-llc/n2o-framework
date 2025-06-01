package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAlertAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.isInvalidColor;

@Component
public class AlertActionValidator implements SourceValidator<N2oAlertAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAlertAction.class;
    }

    @Override
    public void validate(N2oAlertAction source, SourceProcessor p) {
        if (source.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    String.format("Действие <alert> ссылается на несуществующий источник данных %s",
                            ValidationUtils.getIdOrEmptyString(source.getDatasourceId())));

        if (isInvalidColor(source.getColor())) {
            throw new N2oMetadataValidationException(
                    String.format("Действие <alert> использует недопустимое значение атрибута color=\"%s\"",
                            source.getColor()));
        }
    }
}