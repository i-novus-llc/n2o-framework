package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.ContextProvider;

import java.util.*;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class RecursiveFirstLvlErrorProvider implements ContextProvider {

    @Override
    public Map<String, Object> get(Context ctx) {
        Map<String, Object> res = new HashMap<>();
        res.put("recursiveKey3", ctx.get("recursiveKey5"));
        res.put("recursiveKey4", "recursiveKey4");
        return res;
    }

    @Override
    public Set<String> getParams() {
        return new HashSet<>(Arrays.asList("recursiveKey3", "recursiveKey4"));
    }

    @Override
    public Set<String> getDependsOnParams() {
        return new HashSet<>(Arrays.asList("recursiveKey5", "recursiveKey4"));
    }
}
