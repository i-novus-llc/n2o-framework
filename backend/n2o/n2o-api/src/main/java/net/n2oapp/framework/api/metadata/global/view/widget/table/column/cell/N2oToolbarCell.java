package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;

/**
 * Ячейка с кнопками.
 */
@Getter
@Setter
public class N2oToolbarCell extends N2oAbstractCell {
    private String[] generate;
    @VisualAttribute
    private ToolbarItem[] items;
}
