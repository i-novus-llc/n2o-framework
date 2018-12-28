package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oWidgetList;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.table.cell.CellIOv2;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись виджета-списка. * */
@Component
public class ListWidgetElementIOv4 extends WidgetElementIOv4<N2oWidgetList>{

    private Namespace cellDefaultNamespace = CellIOv2.NAMESPACE;
    @Override
    public void io(Element e, N2oWidgetList m, IOProcessor p) {
        super.io(e, m, p);
        p.child(e,null, "rows", m::getColumn,m::setColumn, N2oSimpleColumn.class, this::rows);
    }


    private void rows(Element e, N2oSimpleColumn column, IOProcessor p) {
        p.attribute(e,"text-field-id", column::getId, column::setId);
        p.anyChild(e, null, column::getCell, column::setCell, p.anyOf(N2oCell.class), cellDefaultNamespace);
    }

    @Override
    public String getElementName() {
        return "list";
    }

    @Override
    public Class<N2oWidgetList> getElementClass() {
        return N2oWidgetList.class;
    }
}
