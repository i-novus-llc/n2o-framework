package net.n2oapp.framework.config.metadata.validation.standard.event;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.N2oStompEvent;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
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
        if (source.getAction() != null)
            p.validate(source.getAction());
    }
}
