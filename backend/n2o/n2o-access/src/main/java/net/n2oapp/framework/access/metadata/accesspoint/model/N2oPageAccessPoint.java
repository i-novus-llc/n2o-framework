package net.n2oapp.framework.access.metadata.accesspoint.model;


import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.util.Objects;

/**
 * Точка доступа к странице
 */

public class N2oPageAccessPoint extends AccessPoint {

    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        N2oPageAccessPoint that = (N2oPageAccessPoint) o;

        return Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (page != null ? page.hashCode() : 0);
        return result;
    }
}
