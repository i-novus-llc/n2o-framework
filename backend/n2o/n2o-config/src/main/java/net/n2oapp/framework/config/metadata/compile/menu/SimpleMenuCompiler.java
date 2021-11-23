package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Компиляция простого меню
 */
@Component
public class SimpleMenuCompiler implements BaseSourceCompiler<SimpleMenu, N2oSimpleMenu, ApplicationContext>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public SimpleMenu compile(N2oSimpleMenu source, ApplicationContext context, CompileProcessor p) {
        SimpleMenu simpleMenu = new SimpleMenu();
        ArrayList<HeaderItem> items = new ArrayList<>();
        PageRoutes pageRoutes = new PageRoutes();
        IndexScope idx = p.getScope(IndexScope.class) != null ? p.getScope(IndexScope.class) : new IndexScope();
        if (source != null && source.getMenuItems() != null)
            for (N2oSimpleMenu.MenuItem mi : source.getMenuItems())
                items.add(createMenuItem(mi, idx, context, p, pageRoutes));
        simpleMenu.setItems(items);
        return simpleMenu;
    }

    private HeaderItem createMenuItem(N2oSimpleMenu.MenuItem mi, IndexScope idx, ApplicationContext context,
                                      CompileProcessor p, PageRoutes pageRoutes) {
        HeaderItem item = new HeaderItem();
        if (mi instanceof N2oSimpleMenu.DividerItem) {
            item.setType(((N2oSimpleMenu.DividerItem) mi).getType());
            return item;
        }
        item.setPageId(mi.getPageId());
        item.setId(p.cast(mi.getId(), "menuItem" + idx.get()));
        item.setTitle(mi.getLabel());
        item.setIcon(mi.getIcon());
        item.setImage(mi.getImage());
        item.setTarget(mi.getTarget());
        item.setBadge(mi.getBadge());
        item.setBadgeColor(mi.getBadgeColor());
        item.setImageSrc(mi.getImage());
        if (mi.getAction() != null)
            item.setAction(p.compile(mi.getAction(), context, pageRoutes));
        item.setLinkType(mi instanceof N2oSimpleMenu.AnchorItem ?
                HeaderItem.LinkType.outer :
                HeaderItem.LinkType.inner);
        if (mi.getSubMenu() == null || mi.getSubMenu().length == 0)
            createLinkItem(mi, item, p);
        else {
            ArrayList<HeaderItem> subItems = new ArrayList<>();
            for (N2oSimpleMenu.MenuItem subMenu : mi.getSubMenu())
                subItems.add(createMenuItem(subMenu, idx, context, p, pageRoutes));
            item.setSubItems(subItems);
            item.setHref(item.getSubItems().get(0).getHref());
            item.setType("dropdown");
        }
        if (mi instanceof N2oSimpleMenu.SubMenuItem && ((N2oSimpleMenu.SubMenuItem) mi).getMenuItems() != null) {
            item.setTitle(((N2oSimpleMenu.SubMenuItem) mi).getName());
            item.setImageShape(((N2oSimpleMenu.SubMenuItem) mi).getImageShape());
            ArrayList<HeaderItem> subItems = new ArrayList<>();
            for (N2oSimpleMenu.MenuItem subMenu : ((N2oSimpleMenu.SubMenuItem) mi).getMenuItems())
                subItems.add(createMenuItem(subMenu, idx, context, p, pageRoutes));
            item.setSubItems(subItems);
        }

        item.setProperties(p.mapAttributes(mi));
        return item;
    }

    private void createLinkItem(N2oSimpleMenu.MenuItem mi, HeaderItem item, CompileProcessor p) {
        if (mi.getPageId() == null)
            item.setHref(mi.getHref());
        else {
            N2oPage page = p.getSource(mi.getPageId(), N2oPage.class);
            if (item.getTitle() == null)
                item.setTitle(page.getName() == null ? page.getId() : page.getName());
            if (mi.getRoute() == null)
                item.setHref(page.getRoute() == null ? "/" + mi.getPageId() : page.getRoute());
            else
                item.setHref(mi.getRoute());
            PageContext pageContext = new PageContext(mi.getPageId(), item.getHref());
            p.addRoute(pageContext);
        }
        item.setType("link");
    }
}
