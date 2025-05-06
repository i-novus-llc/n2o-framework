package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCustomCell;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись настраиваемой ячейки
 */
@Component
public class CustomCellElementIOv3 extends AbstractActionCellElementIOv3<N2oCustomCell> {

    @Override
    public String getElementName() {
        return "cell";
    }

    @Override
    public Class<N2oCustomCell> getElementClass() {
        return N2oCustomCell.class;
    }
}
