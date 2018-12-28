package net.n2oapp.framework.api.user;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.context.ContextEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Контекст пользователя
 */
public class UserContext implements Context {

    public static final String USERNAME = "username";
    public static final String CONTEXT = "contextId";
    public static final String SESSION = "sessionId";

    private ContextEngine ctx;

    //cache
    private Map<String, Object> requestCache = new HashMap<>();

    public UserContext(ContextEngine context) {
        this.ctx = context;
    }

    public UserContext(ContextEngine context, Map<String, Object> requestCache) {
        this.ctx = context;
        this.requestCache = requestCache;
    }

    /**
     * Получение имени текущего пользователя
     */
    public String getUsername() {
        Object result = get(USERNAME);
        return result != null ? result.toString() : "";
    }

    /**
     * Получение идентификатора контекста текущего пользователя
     */
    public String getContextId() {
        Object result = get(CONTEXT);
        return result != null ? result.toString() : "";
    }

    /**
     * Получение идентификатора сессии текущего пользователя
     */
    public String getSessionId() {
        Object result = get(SESSION);
        return result != null ? result.toString() : "";
    }

    /**
     * Получение свойств из контекста текущего пользователя
     * @param name - имя свойства
     * @return значение
     */
    @Override
    public Object get(String name) {
        if (ctx == null)
            return null;
        Object result = ctx.get(name, requestCache);
        requestCache.put(name, result);
        return result;
    }

    /**
     * Сохранение свойств в конеткст текщего пользователя
     * @param dataSet - значения
     */
    @Override
    public void set(Map<String, Object> dataSet) {
        if (ctx == null)
            return;
        ctx.set(dataSet, requestCache);
        requestCache.putAll(dataSet);
    }

}
