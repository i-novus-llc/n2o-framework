package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.context.smart.impl.api.RootOneValuePersistentContextProvider;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class RecursiveFirstLvlErrorPersistentProvider implements RootOneValuePersistentContextProvider {

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getParam() {
        return "recursiveKey6";
    }

    @Override
    public void set(Object value) {

    }
}
