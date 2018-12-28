package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.context.smart.impl.api.RootOneValuePersistentContextProvider;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class CabinetPersistentProvider implements RootOneValuePersistentContextProvider {


    private Integer cabinetId = 1;

    @Override
    public void set(Object value) {
        cabinetId = (Integer) value;
    }

    @Override
    public Object getValue() {
        return cabinetId;
    }

    @Override
    public String getParam() {
        return "cab.id";
    }

    @Override
    public boolean isCacheable() {
        return true;
    }
}
