package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;

/**
 * Чтение\запись тулбара в N2oField
 */
public class FieldToolbarIO extends ToolbarIO {

    @Override
    public void io(Element e, N2oToolbar m, IOProcessor p) {
        p.anyChildren(e, null, m::getItems, m::setItems,
                p.anyOf(ToolbarItem.class).ignore("group"),
                buttonNamespace);
    }
}
