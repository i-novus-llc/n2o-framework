package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.RootPersistentContextProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class OrgStructurePersistentProvider implements RootPersistentContextProvider {


    Map<String, Object> res = new HashMap<>();

    {
        res.put("org.id", 1);
        res.put("dep.id", 1);
    }

    @Override
    public void set(Context ctx, Map<String, Object> values) {
        res.putAll(values);
    }

    @Override
    public Map<String, Object> get(Context ctx) {
        return res;
    }

    @Override
    public Set<String> getParams() {
        return res.keySet();
    }

    @Override
    public boolean isCacheable() {
        return true;
    }
}
