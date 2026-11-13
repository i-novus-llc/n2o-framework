package net.n2oapp.framework.config.metadata.validation.standard.event;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.N2oStompEvent;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор события, приходящего через STOMP протокол
 */
@Component
public class StompEventValidator extends TypedMetadataValidator<N2oStompEvent> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStompEvent.class;
    }

    @Override
    public void validate(N2oStompEvent source, SourceProcessor p) {
        if (StringUtils.isBlank(source.getDestination())) {
            throw new N2oMetadataValidationException(
                    "В событии \"<stomp-event>\" не задан обязательный атрибут 'destination'");
        }

        if (source.getAction() != null)
            p.validate(source.getAction());
    }
}