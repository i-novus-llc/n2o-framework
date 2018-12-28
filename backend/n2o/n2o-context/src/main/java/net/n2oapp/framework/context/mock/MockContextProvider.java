package net.n2oapp.framework.context.mock;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.context.smart.impl.api.RootContextProvider;

import java.util.*;

/**
 * Контекст заглушка для тестов
 */
public class MockContextProvider implements RootContextProvider {

    public static final HashSet<String> PARAMS = new HashSet<>(Arrays.asList(UserContext.USERNAME, UserContext.CONTEXT, UserContext.SESSION));

    @Override
    public Map<String, Object> get(Context ctx) {
        Map<String, Object> map = new HashMap<>();
        map.put(UserContext.SESSION, "mock");
        map.put(UserContext.USERNAME, "mock");
        map.put(UserContext.CONTEXT, "mock");
        return map;
    }

    @Override
    public Set<String> getParams() {
        return PARAMS;
    }

    @Override
    public boolean isCacheable() {
        return false;
    }
}
