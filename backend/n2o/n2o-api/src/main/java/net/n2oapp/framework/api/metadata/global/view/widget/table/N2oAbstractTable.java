package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;

@Getter
@Setter
public abstract class N2oAbstractTable extends N2oAbstractListWidget {
    private N2oAbstractColumn[] columns;
    private Boolean autoSelect;
    private RowSelectionEnum selection;
    private String width;
    private String height;
    private Boolean textWrap;
    private Boolean stickyHeader;
    private Boolean stickyFooter;
    private ScrollbarPositionTypeEnum scrollbarPosition;
}
