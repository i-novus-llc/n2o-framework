package net.n2oapp.framework.config.io.toolbar.v2;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;

/**
 * Чтение\запись тулбара в N2oField версии 2.0
 */
public class FieldToolbarIOv2 extends ToolbarIOv2 {

    @Override
    public void io(Element e, N2oToolbar m, IOProcessor p) {
        p.anyChildren(e, null, m::getItems, m::setItems,
                p.anyOf(ToolbarItem.class).ignore("group"),
                buttonNamespace);
    }
}
