package net.n2oapp.framework.config.io.widget.tile;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ComponentIO;
import net.n2oapp.framework.config.io.widget.TilesWidgetIOV4;
import net.n2oapp.framework.config.io.widget.table.cell.CellIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись блока (плитки)
 */
@Component
public class BlockComponentIO extends ComponentIO<N2oTiles.Block> {

    @Override
    public Class<N2oTiles.Block> getElementClass() {
        return N2oTiles.Block.class;
    }

    @Override
    public String getElementName() {
        return "block";
    }

    @Override
    public String getNamespaceUri() {
        return TilesWidgetIOV4.NAMESPACE.getURI();
    }

    @Override
    public void io(Element e, N2oTiles.Block t, IOProcessor p) {
        super.io(e, t, p);
        p.attribute(e, "id", t::getId, t::setId);
        p.anyChild(e, null, t::getComponent, t::setComponent, p.anyOf(N2oCell.class), CellIOv2.NAMESPACE);
    }

}