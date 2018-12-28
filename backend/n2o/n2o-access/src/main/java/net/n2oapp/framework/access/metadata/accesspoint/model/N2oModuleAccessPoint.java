package net.n2oapp.framework.access.metadata.accesspoint.model;


import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class N2oModuleAccessPoint extends AccessPoint {


    private String module;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        N2oModuleAccessPoint that = (N2oModuleAccessPoint) o;

        return module != null ? module.equals(that.module) : that.module == null;

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (module != null ? module.hashCode() : 0);
        return result;
    }
}
