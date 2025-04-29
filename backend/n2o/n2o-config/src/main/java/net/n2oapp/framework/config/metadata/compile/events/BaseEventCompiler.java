package net.n2oapp.framework.config.metadata.compile.events;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.meta.event.Event;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

/**
 * Базовая компиляция события
 */
public abstract class BaseEventCompiler<S extends N2oAbstractEvent, D extends Event>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void initEvents(Event event, S source) {
        event.setId(source.getId());
    }
}
