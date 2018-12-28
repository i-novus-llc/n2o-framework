package net.n2oapp.framework.api.metadata.global.view.widget.table;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;


/**
 * User: iryabov
 * Date: 05.02.13
 * Time: 18:30
 */
public class N2oTable extends N2oAbstractTable<N2oTable> {
    private FilterPosition filterPosition;
    private Boolean filterOpened;
    private NamespaceUriAware[] filters;


    @Override
    public boolean isNavSupport() {
        return true;
    }

    public NamespaceUriAware[] getFilters() {
        return filters;
    }

    public void setFilters(NamespaceUriAware[] filters) {
        this.filters = filters;
    }

    public FilterPosition getFilterPosition() {
        return filterPosition;
    }

    public void setFilterPosition(FilterPosition filterPosition) {
        this.filterPosition = filterPosition;
    }

    public Boolean getFilterOpened() {
        return filterOpened;
    }

    public void setFilterOpened(Boolean filterOpened) {
        this.filterOpened = filterOpened;
    }

    public static enum FilterPosition {
        top, left;

        public String getName() {
            return this.name();
        }
    }
}
