package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;

@Getter
@Setter
public abstract class N2oAbstractTable extends N2oWidget {
    private AbstractColumn[] columns;
    private N2oRow rows;
    private N2oPagination pagination;
    private PagingMode pagingMode;
    private Boolean alwaysShowCount;
    private Boolean hasCheckboxes;
    private Boolean autoSelect;
    private Boolean selected;
    private Size tableSize;
    private String scrollX;
    private String scrollY;

    public enum PagingMode {
        on, off, lazy
    }
}
