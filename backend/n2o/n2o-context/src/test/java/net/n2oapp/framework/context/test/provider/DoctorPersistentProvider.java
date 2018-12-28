package net.n2oapp.framework.context.test.provider;

import net.n2oapp.framework.context.smart.impl.api.OneValueOneDependsOnPersistentContextProvider;


public class DoctorPersistentProvider implements OneValueOneDependsOnPersistentContextProvider {


    private Integer doctorId = 1;

    @Override
    public String getDependsOn() {
        return "dep.id";
    }

    @Override
    public Object getValue(Object dependsOnValue) {
        doctorId = (Integer) dependsOnValue;
        return doctorId;
    }

    @Override
    public void set(Object dependsOnValue, Object value) {
        doctorId = (Integer) value;
    }

    @Override
    public String getParam() {
        return "doctor.id";
    }

}
