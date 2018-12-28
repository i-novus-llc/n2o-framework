package net.n2oapp.framework.context.mock;

import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.context.smart.impl.api.RootOneValuePersistentContextProvider;

/**
 * Тестовая реализация провайдера для получения contextId.
 */
public class MockUserContextProvider implements RootOneValuePersistentContextProvider {
    private String contextId;

    @Override
    public String getParam() {
        return UserContext.CONTEXT;
    }

    @Override
    public Object getValue() {
        return contextId;
    }

    @Override
    public void set(Object value) {
        contextId = (String) value;
    }
}
