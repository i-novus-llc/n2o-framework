package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.table.v5.cell.CellIOv3;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись виджета-списка.
 */
@Component
public class ListWidgetElementIOv5 extends AbstractListWidgetElementIOv5<N2oListWidget> {

    private Namespace cellDefaultNamespace = CellIOv3.NAMESPACE;

    @Override
    public void io(Element e, N2oListWidget m, IOProcessor p) {
        super.io(e, m, p);

        p.anyChildren(e, "content", m::getContent, m::setContent, p.oneOf(N2oListWidget.ContentElement.class)
                .add("left-top", N2oListWidget.LeftTop.class, this::element)
                .add("left-bottom", N2oListWidget.LeftBottom.class, this::element)
                .add("header", N2oListWidget.Header.class, this::element)
                .add("body", N2oListWidget.Body.class, this::element)
                .add("sub-header", N2oListWidget.SubHeader.class, this::element)
                .add("right-top", N2oListWidget.RightTop.class, this::element)
                .add("right-bottom", N2oListWidget.RightBottom.class, this::element)
                .add("extra", N2oListWidget.Extra.class, this::element));
    }

    public void element(Element e, N2oListWidget.ContentElement m, IOProcessor p) {
        p.attribute(e, "text-field-id", m::getTextFieldId, m::setTextFieldId);
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
