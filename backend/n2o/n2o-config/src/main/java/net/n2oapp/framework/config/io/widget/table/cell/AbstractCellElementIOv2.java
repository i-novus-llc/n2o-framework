package net.n2oapp.framework.config.io.widget.table.cell;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom.Element;


/**
 * Чтение\запись абстрактнрой ячейки
 */
public abstract class AbstractCellElementIOv2<T extends N2oAbstractCell> implements NamespaceIO<T>, CellIOv2 {

    @Override
    public void io(Element e, T с, IOProcessor p) {
        p.attribute(e, "src", с::getSrc, с::setSrc);
        p.attribute(e, "class", с::getCssClass, с::setCssClass);
    }

}