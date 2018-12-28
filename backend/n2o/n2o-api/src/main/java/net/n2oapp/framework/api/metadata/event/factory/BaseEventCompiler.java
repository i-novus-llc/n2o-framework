package net.n2oapp.framework.api.metadata.event.factory;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

/**
 * @author operehod
 * @since 23.10.2015
 * Компилятор не учитывающий контекст. Срабатывает если не найден более подходящий (по контексту) компилятор
 */
public abstract class BaseEventCompiler<E extends N2oAction> implements EventCompiler<E, CompileContext, Object> {

    @Override
    public E compile(E e, N2oCompiler compiler, CompileContext context, Object item) {
        return compile(e, compiler);
    }

    @Override
    public Class<CompileContext> getContextClass() {
        return CompileContext.class;
    }

    protected abstract E compile(E e, N2oCompiler compiler);

}
