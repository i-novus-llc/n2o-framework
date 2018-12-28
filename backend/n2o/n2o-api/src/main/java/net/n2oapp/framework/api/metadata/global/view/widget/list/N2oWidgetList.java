package net.n2oapp.framework.api.metadata.global.view.widget.list;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;

/**
 * @author iryabov
 * @since 17.06.2015
 */
public class N2oWidgetList  extends N2oWidget {
    private N2oSimpleColumn column;

    public N2oSimpleColumn getColumn() {
        return column;
    }

    public void setColumn(N2oSimpleColumn column) {
        this.column = column;
    }
}
