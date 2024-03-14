package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import net.n2oapp.framework.config.util.StylesResolver;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.StringUtils.hasLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция простого меню
 */
@Component
public class SimpleMenuCompiler implements BaseSourceCompiler<SimpleMenu, N2oSimpleMenu, ApplicationContext>, SourceClassAware {

    private static final String PROPERTY_PREFIX = "n2o.api.menu.item";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public SimpleMenu compile(N2oSimpleMenu source, ApplicationContext context, CompileProcessor p) {
        SimpleMenu simpleMenu = new SimpleMenu();
        List<MenuItem> items = new ArrayList<>();
        simpleMenu.setProperties(p.mapAttributes(source));
        IndexScope idx = castDefault(p.getScope(IndexScope.class), () -> new IndexScope(1));
        if (ArrayUtils.isNotEmpty(source.getMenuItems())) {
            for (N2oSimpleMenu.AbstractMenuItem mi : source.getMenuItems())
                items.add(createMenuItem(mi, p, context, idx));
        }
        simpleMenu.setItems(items);

        return simpleMenu;
    }

    private MenuItem createMenuItem(N2oSimpleMenu.AbstractMenuItem source,
                                    CompileProcessor p, ApplicationContext context, IndexScope idx) {
        MenuItem compiled = new MenuItem();
        source.setId(castDefault(source.getId(), "mi" + idx.get()));
        compiled.setId(source.getId());
        compiled.setIcon(source.getIcon());
        compiled.setIconPosition(castDefault(source.getIconPosition(),
                () -> p.resolve(property("n2o.api.menu.item.icon_position"), Position.class)));
        compiled.setImageSrc(p.resolveJS(source.getImage()));
        compiled.setImageShape(source.getImageShape());

        if (hasLink(source.getName()) && (source.getDatasourceId() == null &&
                p.getScope(ComponentScope.class).unwrap(DatasourceIdAware.class).getDatasourceId() == null))
            throw new N2oException(
                    String.format("Меню имеет плейсхолдер name=%s, но при этом не указан источник данных",
                            ValidationUtils.getIdOrEmptyString(source.getName())));

        compiled.setTitle(p.resolveJS(source.getName()));

        if (source.getDatasourceId() != null)
            compiled.setDatasource(source.getDatasourceId());
        else if (p.getScope(ComponentScope.class) != null) {
            compiled.setDatasource(p.getScope(ComponentScope.class).unwrap(DatasourceIdAware.class).getDatasourceId());
        }

        if (source instanceof N2oSimpleMenu.MenuItem) {
            menuItem((N2oSimpleMenu.MenuItem) source, compiled, p, context);
        } else if (source instanceof N2oSimpleMenu.DropdownMenuItem) {
            initDropdownMenu((N2oSimpleMenu.DropdownMenuItem) source, compiled, p, context, idx);
        }

        compiled.setProperties(p.mapAttributes(source));

        return compiled;
    }

    private void menuItem(N2oSimpleMenu.MenuItem source, MenuItem compiled, CompileProcessor p, ApplicationContext context) {
        compiled.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));
        compiled.setSrc(initSrc(source.getSrc(), source.getAction(), p));
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setPageId(initPageId(source.getAction()));
        if (source.getName() == null)
            compiled.setTitle(p.resolveJS(initDefaultName(source.getAction(), p)));
        if (source.getAction() != null) {
            Action action = p.compile(
                    source.getAction(),
                    context,
                    new ComponentScope(source, p.getScope(ComponentScope.class))
            );
            if (action instanceof LinkAction) {
                LinkAction linkAction = (LinkAction) action;
                compiled.setHref(linkAction.getUrl());
                compiled.setPathMapping(linkAction.getPathMapping());
                compiled.setQueryMapping(linkAction.getQueryMapping());
                compiled.setTarget(linkAction.getTarget());
                if (linkAction.getTarget().equals(Target.application))
                    compiled.setLinkType(MenuItem.LinkType.inner);
                else
                    compiled.setLinkType(MenuItem.LinkType.outer);
            } else {
                compiled.setAction(action);
            }
        }
    }

    private String initSrc(String src, N2oAction action, CompileProcessor p) {
        if (src != null)
            return src;
        if (action instanceof N2oAnchor || action instanceof N2oOpenPage)
            return p.resolve(property("n2o.api.menu.item.link.src"), String.class);
        if (action != null)
            return p.resolve(property("n2o.api.menu.item.action.src"), String.class);

        return p.resolve(property("n2o.api.menu.item.static.src"), String.class);
    }

    private String initPageId(N2oAction action) {
        return action instanceof N2oAbstractPageAction ? ((N2oAbstractPageAction) action).getPageId() : null;
    }

    private String initDefaultName(N2oAction action, CompileProcessor p) {
        String pageId = initPageId(action);

        return pageId == null
                ? null
                : p.getSource(pageId, N2oPage.class).getName();
    }

    private void initDropdownMenu(N2oSimpleMenu.DropdownMenuItem source, MenuItem item, CompileProcessor p, ApplicationContext context, IndexScope idx) {
        item.setSrc(castDefault(source.getSrc(), () -> p.resolve(property("n2o.api.menu.dropdown.src"), String.class)));
        ArrayList<MenuItem> subItems = new ArrayList<>();
        for (N2oSimpleMenu.AbstractMenuItem subItem : source.getMenuItems()) {
            subItem.setDatasourceId(castDefault(subItem.getDatasourceId(), source.getDatasourceId()));
            subItems.add(createMenuItem(subItem, p, context, idx));
        }
        item.setSubItems(subItems);
    }
}