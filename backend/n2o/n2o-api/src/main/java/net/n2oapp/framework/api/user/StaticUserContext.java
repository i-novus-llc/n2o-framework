package net.n2oapp.framework.api.user;

import net.n2oapp.framework.api.context.ContextEngine;

/**
 * Статический доступ к контексту текущего пользователя
 * @author iryabov
 * @since 22.07.2016
 */
public class StaticUserContext {
    protected static ContextEngine context;

    public StaticUserContext(ContextEngine context) {
        setContext(context);
    }

    /**
     * Установка контекста (может быть сделана только один раз)
     * @throws IllegalStateException - если установка была сделана повторно
     * @param contextEngine - сервис для получения контекста текущего пользователя
     */
    protected void setContext(ContextEngine contextEngine) {
        StaticUserContext.context = contextEngine;
    }

    public static UserContext getUserContext() {
        return new UserContext(context);
    }

}
