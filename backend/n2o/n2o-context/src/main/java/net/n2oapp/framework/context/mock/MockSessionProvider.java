package net.n2oapp.framework.context.mock;

import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.context.smart.impl.api.RootOneValueContextProvider;

/**
 * Тестовая реализация провайдера для получения sessionId.
 */
public class MockSessionProvider implements RootOneValueContextProvider {

    @Override
    public String getParam() {
        return UserContext.SESSION;
    }

    @Override
    public Object getValue() {
        return "";
    }
}
