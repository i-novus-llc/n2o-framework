package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oCards;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.Cards;
import net.n2oapp.framework.config.io.cell.v2.CellIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись виджета Карточки
 */
@Component
public class CardsWidgetIOV4 extends AbstractListWidgetElementIOv4<N2oCards> {

    @Override
    public String getElementName() {
        return "cards";
    }

    @Override
    public Class<N2oCards> getElementClass() {
        return N2oCards.class;
    }

    @Override
    public void io(Element e, N2oCards t, IOProcessor p) {
        super.io(e, t, p);
        p.attributeEnum(e, "vertical-align", t::getVerticalAlign, t::setVerticalAlign, Cards.Position.class);
        p.attribute(e, "height", t::getHeight, t::setHeight);
        p.children(e, "content", "col", t::getContent, t::setContent, N2oCards.Col::new, this::col);
    }

    private void col(Element e, N2oCards.Col col, IOProcessor p) {
        p.attributeInteger(e, "size", col::getSize, col::setSize);
        p.children(e, null, "block", col::getBlocks, col::setBlocks, N2oCards.Block::new, this::block);
    }

    private void block(Element e, N2oCards.Block b, IOProcessor p) {
        p.attribute(e, "id", b::getId, b::setId);
        p.attribute(e, "text-field-id", b::getTextFieldId, b::setTextFieldId);
        p.attribute(e, "tooltip-field-id", b::getTooltipFieldId, b::setTooltipFieldId);
        p.attribute(e, "class", b::getCssClass, b::setCssClass);
        p.attribute(e, "style", b::getStyle, b::setStyle);
        p.anyChild(e, null, b::getComponent, b::setComponent, p.anyOf(N2oCell.class), CellIOv2.NAMESPACE);
    }

}
