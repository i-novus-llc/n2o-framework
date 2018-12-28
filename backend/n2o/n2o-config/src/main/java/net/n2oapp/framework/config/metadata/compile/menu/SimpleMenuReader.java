package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChildren;

/**
 * Read simple menu
 *
 * @author igafurov
 * @since 13.04.2017
 */
public class SimpleMenuReader extends AbstractFactoredReader<N2oSimpleMenu> {
    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/simple-menu-1.0";
    }

    @Override
    public N2oSimpleMenu read(Element element, Namespace namespace) {
        N2oSimpleMenu menu = new N2oSimpleMenu();
        String refId = ReaderJdomUtil.getAttributeString(element, "ref-id");
        if (refId != null) menu.setRefId(refId);
        menu.setSrc(getAttributeString(element, "src"));
        menu.setNamespaceUri(getNamespaceUri());
        List<N2oSimpleMenu.MenuItem> menuItems = new ArrayList<>();
        for (Element child : (List<Element>) element.getChildren()) {
            N2oSimpleMenu.MenuItem item = new SimpleMenuItemReader().read(child);
            menuItems.add(item);
        }
        menu.setMenuItems(menuItems.toArray(new N2oSimpleMenu.MenuItem[menuItems.size()]));
        return menu;
    }

    @Override
    public Class<N2oSimpleMenu> getElementClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public String getElementName() {
        return "menu";
    }

    private static class SimpleMenuItemReader implements TypedElementReader<N2oSimpleMenu.MenuItem> {
        @Override
        public N2oSimpleMenu.MenuItem read(Element element) {
            N2oSimpleMenu.MenuItem item = null;
            if (element.getName().equals("page")) {
                item = new N2oSimpleMenu.PageItem();
                item.setPageId(getAttributeString(element, "page-id"));
                item.setRoute(getAttributeString(element, "route"));
            }
            if (element.getName().equals("a")) {
                item = new N2oSimpleMenu.AnchorItem();
                item.setHref(getAttributeString(element, "href"));
            }
            if (item == null)
                item = new N2oSimpleMenu.MenuItem();
            if (element.getName().equals("sub-menu")) {
                item.setSubMenu(getChildren(element, null, null, new SimpleMenuItemReader()));
            }
            item.setTarget(ReaderJdomUtil.getAttributeEnum(element, "target", Target.class));
            item.setLabel(getAttributeString(element, "label"));
            item.setIcon(getAttributeString(element, "icon"));
            return item;
        }

        public Class<N2oSimpleMenu.MenuItem> getElementClass() {
            return N2oSimpleMenu.MenuItem.class;
        }

        @Override
        public String getElementName() {
            return "temp";
        }
    }
}
