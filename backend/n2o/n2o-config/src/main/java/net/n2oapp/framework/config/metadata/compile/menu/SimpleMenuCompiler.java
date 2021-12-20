package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        List<HeaderItem> items = new ArrayList<>();
        IndexScope idx = p.getScope(IndexScope.class) != null ? p.getScope(IndexScope.class) : new IndexScope(1);
        if (source.getMenuItems() != null)
            for (N2oSimpleMenu.AbstractMenuItem mi : source.getMenuItems())
                items.add(createMenuItem(mi, p, context, idx));
        simpleMenu.setItems(items);
        return simpleMenu;
    }

    private HeaderItem createMenuItem(N2oSimpleMenu.AbstractMenuItem source,
                                      CompileProcessor p, ApplicationContext context, IndexScope idx) {
        HeaderItem compiled = new HeaderItem();
        source.setId(p.cast(source.getId(), "mi" + idx.get()));
        compiled.setId(source.getId());
        compiled.setTitle(source.getName());
        compiled.setIcon(source.getIcon());
        compiled.setImageSrc(source.getImage());
        compiled.setImageShape(source.getImageShape());
        compiled.setDatasource(source.getDatasource());
        if (source instanceof N2oSimpleMenu.MenuItem) {
            menuItem((N2oSimpleMenu.MenuItem) source, compiled, p, context);
        }
        else if (source instanceof N2oSimpleMenu.DropdownMenuItem) {
            dropdownMenu((N2oSimpleMenu.DropdownMenuItem) source, compiled, p, context, idx);
        }
        compiled.setProperties(p.mapAttributes(source));
        return compiled;
    }

    private void menuItem(N2oSimpleMenu.MenuItem source, HeaderItem compiled, CompileProcessor p, ApplicationContext context) {
        compiled.setBadge(p.resolve(source.getBadge()));
        compiled.setBadgeColor(source.getBadgeColor());
        compiled.setType("link");
        compiled.setPageId(initPageId(source.getAction()));
        if (source.getName() == null)
            compiled.setTitle(initDefaultName(source.getAction(), p));
        if (source.getAction() != null) {
            Action action = p.compile(source.getAction(), context, new ComponentScope(source));
            if (action instanceof LinkAction) {
                LinkAction linkAction = (LinkAction) action;
                compiled.setHref(linkAction.getUrl());
//                compiled.setTarget(linkAction.getTarget());
                if (linkAction.getTarget().equals(Target.application))
                    compiled.setLinkType(HeaderItem.LinkType.inner);
                else
                    compiled.setLinkType(HeaderItem.LinkType.outer);
                //            compiled.setPathMapping(linkAction.getPathMapping());
                //            compiled.setQueryMapping(linkAction.getQueryMapping());
            } else
                throw new N2oException("Action " + action.getClass() + " not supported in menu yet");
        }
    }

    private String initPageId(N2oAction action) {
        return action instanceof N2oAbstractPageAction ? ((N2oAbstractPageAction) action).getPageId() : null;
    }

    private String initDefaultName(N2oAction action, CompileProcessor p) {
        String pageId = initPageId(action);
        if (pageId == null)
            return null;
        N2oPage page = p.getSource(pageId, N2oPage.class);
        return page.getName();
    }

    private void dropdownMenu(N2oSimpleMenu.DropdownMenuItem source, HeaderItem item, CompileProcessor p, ApplicationContext context, IndexScope idx) {
        item.setType("dropdown");
        ArrayList<HeaderItem> subItems = new ArrayList<>();
        for (N2oSimpleMenu.MenuItem subItem : source.getMenuItems())
            subItems.add(createMenuItem(subItem, p, context, idx));
        item.setSubItems(subItems);
    }
}
