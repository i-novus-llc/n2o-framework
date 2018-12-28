package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.context.smart.impl.api.OneValueOneDependsOnPersistentContextProvider;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class RecursiveSecondLvlErrorPersistentProvider implements OneValueOneDependsOnPersistentContextProvider {


    @Override
    public void set(Object dependsOnValue, Object value) {

    }

    @Override
    public Object getValue(Object dependsOnValue) {
        return null;
    }

    @Override
    public String getParam() {
        return "recursiveKey7";
    }

    @Override
    public String getDependsOn() {
        return "recursiveKey6";
    }
}
