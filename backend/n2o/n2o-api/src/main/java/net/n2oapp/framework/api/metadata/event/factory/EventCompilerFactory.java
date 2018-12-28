package net.n2oapp.framework.api.metadata.event.factory;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

/**
 * Фабрика компиляторов действий
 */
public interface EventCompilerFactory {
    EventCompiler produce(Class<? extends N2oAction> eventClass, Class<? extends CompileContext> contextClass,
                          Class<? extends Object> eventContainerClass);
}
