package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;

@Getter
@Setter
public abstract class N2oAbstractTable extends N2oAbstractListWidget {
    @VisualAttribute
    private AbstractColumn[] columns;
    @VisualAttribute
    private Boolean autoSelect;
    @VisualAttribute
    private RowSelectionEnum selection;
    @VisualAttribute
    private Size tableSize;
    @VisualAttribute
    private String width;
    @VisualAttribute
    private String height;
    @VisualAttribute
    private Boolean textWrap;
    @VisualAttribute
    private Boolean checkboxes;
    @VisualAttribute
    private Boolean checkOnSelect;
}
