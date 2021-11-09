package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;

/**
 * Чтение\запись абстрактной ячейки
 */
public abstract class AbstractCellElementIOv3<T extends N2oAbstractCell> implements NamespaceIO<T>, CellIOv3 {

    @Override
    public void io(Element e, T c, IOProcessor p) {
        p.attribute(e, "src", c::getSrc, c::setSrc);
        p.attribute(e, "class", c::getCssClass, c::setCssClass);
        p.attribute(e, "style", c::getStyle, c::setStyle);
        p.attribute(e, "visible", c::getVisible, c::setVisible);
        p.anyAttributes(e, c::getExtAttributes, c::setExtAttributes);
    }
}