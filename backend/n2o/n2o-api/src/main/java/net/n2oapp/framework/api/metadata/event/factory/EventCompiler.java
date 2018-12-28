package net.n2oapp.framework.api.metadata.event.factory;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.local.context.OutOfRangeException;

/**
 * Компилирует какое-то событие под каким-то определенным контекстом.
 * Т.е. можно написать несколько компиляторо под одно событие, но разные контексты
 */
@Deprecated
public interface EventCompiler<E extends N2oAction, C extends CompileContext, I> {
    /**
     * @param e        - "сырое" событие
     * @param compiler - компилятор
     * @param c        - контекст компиляции
     * @return - скомпилированное событие
     */
    E compile(E e, N2oCompiler compiler, C c, I item) throws OutOfRangeException;

    Class<C> getContextClass();

    Class<E> getEventClass();

    Class<I> getEventContainerClass();
}
