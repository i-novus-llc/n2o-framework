package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;

@Getter
@Setter
public abstract class N2oAbstractTable extends N2oAbstractListWidget {
    @N2oAttribute
    private AbstractColumn[] columns;
    @N2oAttribute
    private Boolean autoSelect;
    @N2oAttribute
    private RowSelectionEnum selection;
    @N2oAttribute
    private Size tableSize;
    @N2oAttribute
    private String width;
    @N2oAttribute
    private String height;
    @N2oAttribute
    private Boolean textWrap;
    @N2oAttribute
    private Boolean checkboxes;
    @N2oAttribute
    private Boolean checkOnSelect;
}
