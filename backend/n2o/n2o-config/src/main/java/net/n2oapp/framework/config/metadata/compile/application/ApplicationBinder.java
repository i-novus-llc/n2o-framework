package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.util.BindUtil.resolveExtension;

/**
 * Базовое связывание данных в шапке
 */
@Component
public class ApplicationBinder implements BaseMetadataBinder<Application> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Application.class;
    }

    @Override
    public Application bind(Application compiled, BindProcessor p) {
        bindHeader(p, compiled.getHeader());
        if (compiled.getSidebars() != null) {
            for (Sidebar sidebar : compiled.getSidebars())
                bindSidebar(p, sidebar);
        }

        if (compiled.getDatasources() != null)
            compiled.getDatasources().values().forEach(p::bind);
        return compiled;
    }

    private void bindHeader(BindProcessor p, Header header) {
        if (header != null) {
            bindMenu(header.getMenu(), p);
            bindMenu(header.getExtraMenu(), p);
            resolveExtension(header, p);
        }
    }

    private void bindSidebar(BindProcessor p, Sidebar sidebar) {
        if (sidebar != null) {
            bindMenu(sidebar.getMenu(), p);
            bindMenu(sidebar.getExtraMenu(), p);
            sidebar.setSubtitle(p.resolveText(sidebar.getSubtitle()));
            resolveExtension(sidebar, p);
        }
    }

    private void bindMenu(SimpleMenu menu, BindProcessor p) {
        if (menu != null && menu.getItems() != null)
            menu.getItems()
                    .forEach(i -> resolveItem(i, p));
    }

    private void resolveItem(MenuItem item, BindProcessor p) {
        item.setTitle(p.resolveText(item.getTitle()));
        item.setImageSrc(p.resolve(item.getImageSrc(), String.class));
        resolveExtension(item, p);
        BadgeUtil.bindSimpleBadge(item.getBadge(), p);
        if (item.getSubItems() != null)
            item.getSubItems().forEach(i -> resolveItem(i, p));
    }
}
