package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись виджета Плитки
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
        p.attributeInteger(e, "width", t::getWidth, t::setWidth);
        p.attributeInteger(e, "height", t::getHeight, t::setHeight);
        p.anyChildren(e, "content", t::getContent, t::setContent, p.anyOf(N2oTiles.Block.class), TilesWidgetIOV4.NAMESPACE);
    }
}
