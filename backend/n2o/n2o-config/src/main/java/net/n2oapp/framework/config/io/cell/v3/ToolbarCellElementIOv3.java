package net.n2oapp.framework.config.io.cell.v3;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.GroupItem;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oGroup;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.toolbar.v2.AbstractButtonIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись ячейки с кнопками версии 3.0
 */
@Component
public class ToolbarCellElementIOv3 extends AbstractCellElementIOv3<N2oToolbarCell> {
    protected Namespace buttonNamespace = AbstractButtonIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oToolbarCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeArray(e, "generate", ",", c::getGenerate, c::setGenerate);
        p.anyChildren(e, null, c::getItems, c::setItems,
                p.anyOf(ToolbarItem.class).add("group", buttonNamespace.getURI(), N2oGroup.class, this::group),
                buttonNamespace);
    }

    private void group(Element e, N2oGroup g, IOProcessor p) {
        p.attributeArray(e, "generate", ",", g::getGenerate, g::setGenerate);
        p.anyChildren(e, null, g::getItems, g::setItems, p.anyOf(GroupItem.class), buttonNamespace);
    }

    @Override
    public String getElementName() {
        return "toolbar";
    }

    @Override
    public Class<N2oToolbarCell> getElementClass() {
        return N2oToolbarCell.class;
    }
}
