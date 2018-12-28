package net.n2oapp.framework.context.mock;

import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.context.smart.impl.api.RootOneValueContextProvider;

/**
 * Тестовая реализация провайдера для получения username.
 */
public class MockUsernameProvider implements RootOneValueContextProvider {

    @Override
    public String getParam() {
        return UserContext.USERNAME;
    }
    @Override
    public Object getValue() {
        return "admin";
    }

}
