package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Чтение\запись тулбара.
 */
public class ToolbarIO implements TypedElementIO<N2oToolbar> {

    protected Namespace buttonNamespace = AbstractMenuItemIO.NAMESPACE;

    @Override
    public Class<N2oToolbar> getElementClass() {
        return N2oToolbar.class;
    }

    @Override
    public String getElementName() {
        return "toolbar";
    }

    @Override
    public void io(Element e, N2oToolbar m, IOProcessor p) {
        p.attribute(e, "place", m::getPlace, m::setPlace);
        p.attributeArray(e, "generate", ",", m::getGenerate, m::setGenerate);
        p.anyChildren(e, null, m::getItems, m::setItems,
                p.anyOf(ToolbarItem.class).add("group", buttonNamespace.getURI(), N2oGroup.class, this::group),
                buttonNamespace);
    }

    private void group(Element e, N2oGroup g, IOProcessor p) {
        p.attributeArray(e, "generate", ",", g::getGenerate, g::setGenerate);
        p.anyChildren(e, null, g::getItems, g::setItems, p.anyOf(GroupItem.class), buttonNamespace);
    }

}
