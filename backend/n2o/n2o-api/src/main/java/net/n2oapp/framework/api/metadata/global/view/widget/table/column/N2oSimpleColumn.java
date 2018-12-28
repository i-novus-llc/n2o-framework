package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

/**
 * Столбец таблицы
 */
public class N2oSimpleColumn extends AbstractColumn {

    private N2oCell cell;

    public N2oCell getCell() {
        return cell;
    }

    public void setCell(N2oCell cell) {
        this.cell = cell;
    }


    @Override
    public boolean isDynamic() {
        return false;
    }
}
