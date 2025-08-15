package net.n2oapp.framework.config.metadata.compile.events;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.meta.event.Event;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Базовая компиляция события
 */
public abstract class BaseEventCompiler<S extends N2oAbstractEvent, D extends Event>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void initEvents(Event event, S source, CompileProcessor p) {
        IndexScope indexScope = castDefault(p.getScope(IndexScope.class), IndexScope::new);
        source.setId(castDefault(source.getId(), "ev" + indexScope.get()));
        event.setId(source.getId());
    }
}