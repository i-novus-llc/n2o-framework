package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;

@Getter
@Setter
public abstract class N2oAbstractTable extends N2oAbstractListWidget {
    private AbstractColumn[] columns;
    private PagingMode pagingMode;
    private Boolean alwaysShowCount;
    private Boolean hasCheckboxes;
    private Boolean autoSelect;
    private RowSelectionEnum selection;
    private Size tableSize;
    private String width;
    private String height;
    private Boolean textWrap;
    private Boolean checkboxes;
    private Boolean checkOnSelect;

    public enum PagingMode {
        on, off, lazy
    }
}
