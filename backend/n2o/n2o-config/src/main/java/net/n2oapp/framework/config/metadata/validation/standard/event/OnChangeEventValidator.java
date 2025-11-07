package net.n2oapp.framework.config.metadata.validation.standard.event;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.N2oOnChangeEvent;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkCloseInMultiAction;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;

/**
 * Валидатор события изменения модели данных
 */
@Component
public class OnChangeEventValidator extends TypedMetadataValidator<N2oOnChangeEvent> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOnChangeEvent.class;
    }

    @Override
    public void validate(N2oOnChangeEvent source, SourceProcessor p) {
        if (source.getDatasourceId() == null)
            throw new N2oMetadataValidationException("В событии <on-change> не задан атрибут 'datasource'");
        ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                String.format("Событие <on-change> ссылается на несуществующий источник данных '%s'", source.getDatasourceId()));
        if (ArrayUtils.isEmpty(source.getActions()) && source.getActionId() == null)
            throw new N2oMetadataValidationException(String.format("В событии <on-change> %s не заданы действия", ValidationUtils.getIdOrEmptyString(source.getId())));
        checkOnFailAction(source.getActions());
        checkCloseInMultiAction(source.getActions());
    }
}
