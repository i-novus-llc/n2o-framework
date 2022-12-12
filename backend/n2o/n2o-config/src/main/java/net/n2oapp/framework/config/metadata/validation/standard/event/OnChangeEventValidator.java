package net.n2oapp.framework.config.metadata.validation.standard.event;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.N2oOnChangeEvent;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

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
    }
}
