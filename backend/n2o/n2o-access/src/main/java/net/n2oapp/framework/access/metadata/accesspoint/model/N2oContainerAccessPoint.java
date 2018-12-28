package net.n2oapp.framework.access.metadata.accesspoint.model;


import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

public class N2oContainerAccessPoint extends AccessPoint {

    private String container;
    private String page;

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

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

        N2oContainerAccessPoint that = (N2oContainerAccessPoint) o;

        if (container != null ? !container.equals(that.container) : that.container != null) return false;

        return page != null ? page.equals(that.page) : that.page == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (container != null ? container.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        return result;
    }
}
