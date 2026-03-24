package net.n2oapp.framework.config.metadata.compile.events;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.N2oStompEvent;
import net.n2oapp.framework.api.metadata.meta.event.StompEvent;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция  STOMP-события
 */
@Component
public class StompEventCompiler extends BaseEventCompiler<N2oStompEvent, StompEvent> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStompEvent.class;
    }

    @Override
    public StompEvent compile(N2oStompEvent source, CompileContext<?, ?> context, CompileProcessor p) {
        StompEvent event = new StompEvent();
        initEvents(event, source, p);
        event.setDestination(source.getDestination());
        event.setType(p.resolve(property("n2o.api.page.event.stomp.type"), String.class));
        return event;
    }
}
