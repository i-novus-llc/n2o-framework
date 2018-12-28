package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.context.smart.impl.api.OneValueOneDependsContextProvider;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 17:59
 */
public class CabinetProvider implements OneValueOneDependsContextProvider {


    @Override
    public Object getValue(Object dependsOnValue) {
        return dependsOnValue.toString();
    }

    @Override
    public String getParam() {
        return "cab.name";
    }

    @Override
    public String getDependsOn() {
        return "cab.id";
    }
}
