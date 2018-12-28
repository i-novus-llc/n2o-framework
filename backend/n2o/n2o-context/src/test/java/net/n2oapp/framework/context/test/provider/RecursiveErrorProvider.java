package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.ContextProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class RecursiveErrorProvider implements ContextProvider {

    @Override
    public Map<String, Object> get(Context ctx) {
        ctx.get("recursiveKey1");
        ctx.get("recursiveKey2");
        return null;
    }

    @Override
    public Set<String> getParams() {
        return new HashSet<>(Arrays.asList("recursiveKey1", "recursiveKey2"));
    }

    @Override
    public Set<String> getDependsOnParams() {
        return getParams();
    }
}
