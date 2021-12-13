package net.n2oapp.framework.config.io.cell.v3;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись ячейки с кнопками версии 3.0
 */
@Component
public class ToolbarCellElementIOv3 extends AbstractCellElementIOv3<N2oToolbarCell> {

    @Override
    public void io(Element e, N2oToolbarCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeArray(e, "generate", ",", c::getGenerate, c::setGenerate);
        p.anyChildren(e, null, c::getItems, c::setItems, p.oneOf(ToolbarItem.class)
                .add("button", N2oButton.class, new ButtonIOv2())
                .add("sub-menu", N2oSubmenu.class, new SubmenuIOv2()));
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
