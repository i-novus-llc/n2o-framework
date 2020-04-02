package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oMenuItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись menu-item
 */
@Component
public class MenuItemIO extends AbstractMenuItemIO<N2oMenuItem> {
    @Override
    public Class<N2oMenuItem> getElementClass() {
        return N2oMenuItem.class;
    }

    @Override
    public String getElementName() {
        return "menu-item";
    }

    @Override
    public void io(Element e, N2oMenuItem mi, IOProcessor p) {
        super.io(e, mi, p);
    }
}
