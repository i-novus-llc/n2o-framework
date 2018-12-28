package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.PersistentContextProvider;

import java.util.*;

public class ChangeableDependenciesProvider implements PersistentContextProvider {
    public static boolean hasFirstParent = true;

    Map<String, Object> res = new HashMap<>();

    {
        res.put("changeableDependenciesKey.id", 1);
    }

    @Override
    public Map<String, Object> get(Context ctx) {
        if (hasFirstParent) {
            ctx.get("org.id");
        } else {
            ctx.get("dep.id");
        }
        return res;
    }

    @Override
    public Set<String> getParams() {
        return res.keySet();
    }

    @Override
    public Set<String> getDependsOnParams() {
        return new HashSet<>(Arrays.asList("org.id"));
    }

    @Override
    public void set(Context ctx, Map<String, Object> values) {
        if (hasFirstParent) {
            ctx.get("org.id");
        } else {
            ctx.get("dep.id");
        }
    }
}
