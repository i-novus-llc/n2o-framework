package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.table.cell.CellIOv2;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись виджета-списка. *
 */
@Component
public class ListWidgetElementIOv4 extends WidgetElementIOv4<N2oListWidget> {

    private Namespace cellDefaultNamespace = CellIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oListWidget m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "content", m::getContent, m::setContent, p.oneOf(N2oListWidget.ContentElement.class)
                .add("image", N2oListWidget.Image.class, this::element)
                .add("header", N2oListWidget.Header.class, this::element)
                .add("body", N2oListWidget.Body.class, this::element)
                .add("sub-header", N2oListWidget.SubHeader.class, this::element)
                .add("right-top", N2oListWidget.RightTop.class, this::element)
                .add("right-bottom", N2oListWidget.RightBottom.class, this::element)
                .add("extra", N2oListWidget.Extra.class, this::element));
    }

    public void element(Element e, N2oListWidget.ContentElement m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.anyChild(e, null, m::getCell, m::setCell, p.anyOf(N2oCell.class), cellDefaultNamespace);
    }


    @Override
    public String getElementName() {
        return "list";
    }

    @Override
    public Class<N2oListWidget> getElementClass() {
        return N2oListWidget.class;
    }
}
