package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.table.cell.CellIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись плиток
 */
@Component
public class TilesWidgetIOV4 extends WidgetElementIOv4<N2oTiles> {

    @Override
    public String getElementName() {
        return "tiles";
    }

    @Override
    public Class<N2oTiles> getElementClass() {
        return N2oTiles.class;
    }

    @Override
    public void io(Element e, N2oTiles t, IOProcessor p) {
        super.io(e, t, p);
        p.attributeInteger(e, "cols-sm", t::getColsSm, t::setColsSm);
        p.attributeInteger(e, "cols-md", t::getColsMd, t::setColsMd);
        p.attributeInteger(e, "cols-lg", t::getColsLg, t::setColsLg);
        p.anyChildren(e, "content", t::getContent, t::setContent, p.oneOf(N2oTiles.Block.class)
                .add("block", N2oTiles.Block.class, this::content));
    }

    private void content(Element e, N2oTiles.Block t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attribute(e, "class", t::getClassName, t::setClassName);
        p.attribute(e, "style", t::getStyle, t::setStyle);
        p.attribute(e, "src", t::getSrc, t::setSrc);
        p.anyChild(e, null, t::getComponent, t::setComponent, p.anyOf(N2oCell.class), CellIOv2.NAMESPACE);
    }

}