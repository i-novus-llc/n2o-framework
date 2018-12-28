package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.context.smart.impl.api.OneValueOneDependsContextProvider;

public class DoctorNameProvider implements OneValueOneDependsContextProvider {


    @Override
    public Object getValue(Object dependsOnValue) {
        return dependsOnValue.toString();
    }

    @Override
    public String getParam() {
        return "doctor.name";
    }

    @Override
    public String getDependsOn() {
        return "doctor.id";
    }
}
