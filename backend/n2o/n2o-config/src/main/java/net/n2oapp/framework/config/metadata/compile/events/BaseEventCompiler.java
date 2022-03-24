package net.n2oapp.framework.config.metadata.compile.events;

import net.n2oapp.framework.api.metadata.application.Event;
import net.n2oapp.framework.api.metadata.application.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

/**
 * Базовая компиляция события
 */
public abstract class BaseEventCompiler<S extends N2oAbstractEvent, D extends Event>
    implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void initEvents(Event event, N2oAbstractEvent source, CompileContext<?, ?> context,
                              CompileProcessor p) {
        event.setId(source.getId());
    }
}
