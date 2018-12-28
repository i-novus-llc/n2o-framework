package net.n2oapp.framework.api.metadata.global.view.widget.table;


import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;


public abstract class N2oAbstractTable<T extends N2oAbstractTable> extends N2oWidget<T> {
    private AbstractColumn[] columns;
    private N2oRow rows;
    private N2oPagination pagination;
    private PagingMode pagingMode;
    private Boolean alwaysShowCount;
    private Boolean hasCheckboxes;
    private Boolean autoSelect;

    @Override
    public boolean isNavSupport() {
        return true;
    }

    public N2oRow getRows() {
        return rows;
    }

    public void setRows(N2oRow rows) {
        this.rows = rows;
    }

    public AbstractColumn[] getColumns() {
        return columns;
    }

    public void setColumns(AbstractColumn[] columns) {
        this.columns = columns;
    }


    public N2oPagination getPagination() {
        return pagination;
    }

    public void setPagination(N2oPagination pagination) {
        this.pagination = pagination;
    }

    public PagingMode getPagingMode() {
        return pagingMode;
    }

    public void setPagingMode(PagingMode pagingMode) {
        this.pagingMode = pagingMode;
    }

    public Boolean getAlwaysShowCount() {
        return alwaysShowCount;
    }

    public void setAlwaysShowCount(Boolean alwaysShowCount) {
        this.alwaysShowCount = alwaysShowCount;
    }

    public Boolean getHasCheckboxes() {
        return hasCheckboxes;
    }

    public void setHasCheckboxes(Boolean hasCheckboxes) {
        this.hasCheckboxes = hasCheckboxes;
    }

    public Boolean getAutoSelect() {
        return autoSelect;
    }

    public void setAutoSelect(Boolean autoSelect) {
        this.autoSelect = autoSelect;
    }

    public enum DirectionType {
        NONE,
        ASC,
        DESC
    }

    public enum PagingMode {
        on, off, lazy
    }
}
