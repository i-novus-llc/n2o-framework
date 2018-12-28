package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.ContextProvider;

import java.util.*;

/**
 * User: operehod
 * Date: 28.01.2015
 * Time: 11:29
 */
public class FullDepartmentProvider implements ContextProvider {

    @Override
    public Map<String, Object> get(Context ctx) {
        Map<String, Object> res = new HashMap<>();
        res.put("fullDep.id", ctx.get("org.id").toString() + ctx.get("dep.id").toString());
        res.put("fullDep.name", ctx.get("org.name").toString() + ctx.get("dep.name").toString());
        return res;
    }

    @Override
    public Set<String> getParams() {
        Set<String> res = new HashSet<>();
        res.add("fullDep.id");
        res.add("fullDep.name");
        return res;
    }

    @Override
    public Set<String> getDependsOnParams() {
        return new HashSet<>(Arrays.asList("org.id", "org.name", "dep.id", "dep.name"));
    }
}
