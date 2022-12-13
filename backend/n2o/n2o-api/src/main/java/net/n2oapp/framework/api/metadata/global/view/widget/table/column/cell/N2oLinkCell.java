package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;


/**
 * Ячейка таблицы со ссылкой
 */
@Getter
@Setter
@VisualComponent
public class N2oLinkCell extends N2oActionCell {
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private IconType type;
    @VisualAttribute
    private String url;
    @VisualAttribute
    private Target target;
}
