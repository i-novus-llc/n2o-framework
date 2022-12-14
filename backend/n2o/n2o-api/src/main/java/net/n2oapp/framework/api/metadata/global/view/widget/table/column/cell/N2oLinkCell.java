package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;


/**
 * Ячейка таблицы со ссылкой
 */
@Getter
@Setter
public class N2oLinkCell extends N2oActionCell {
    private String icon;
    private IconType type;
    private String url;
    private Target target;
}
