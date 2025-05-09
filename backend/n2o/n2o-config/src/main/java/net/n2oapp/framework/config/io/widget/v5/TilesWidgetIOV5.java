package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oBlock;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.cell.v3.CellIOv3;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись виджета Плитки
 */
@Component
public class TilesWidgetIOV5 extends AbstractListWidgetElementIOv5<N2oTiles> {

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
        p.attribute(e, "width", t::getWidth, t::setWidth);
        p.attribute(e, "height", t::getHeight, t::setHeight);
        p.children(e, "content", "block", t::getContent, t::setContent, N2oBlock::new, this::block);
        p.merge(t,getElementName());
    }

    private void block(Element e, N2oBlock b, IOProcessor p) {
        p.attribute(e, "id", b::getId, b::setId);
        p.attribute(e, "text-field-id", b::getTextFieldId, b::setTextFieldId);
        p.attribute(e, "tooltip-field-id", b::getTooltipFieldId, b::setTooltipFieldId);
        p.attribute(e, "class", b::getCssClass, b::setCssClass);
        p.attribute(e, "style", b::getStyle, b::setStyle);
        p.anyChild(e, null, b::getComponent, b::setComponent, p.anyOf(N2oCell.class), CellIOv3.NAMESPACE);
    }
}
